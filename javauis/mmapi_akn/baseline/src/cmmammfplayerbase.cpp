/*
* Copyright (c) 2002-2007 Nokia Corporation and/or its subsidiary(-ies).
* All rights reserved.
* This component and the accompanying materials are made available
* under the terms of "Eclipse Public License v1.0"
* which accompanies this distribution, and is available
* at the URL "http://www.eclipse.org/legal/epl-v10.html".
*
* Initial Contributors:
* Nokia Corporation - initial contribution.
*
* Contributors:
*
* Description:  This class is used for playing sounds
*
*/


//  INCLUDE FILES
#include <mmf/server/mmfdes.h>
#include <AudioPreference.h>
#include <jdebug.h>
#include "cmmammfplayerbase.h"
#include "cmmammfresolver.h"
#include <Oma2Agent.h>

CMMAMMFPlayerBase::~CMMAMMFPlayerBase()
{
    // First delete the control and then close the controller
    // Added after AudioOutputControl
    DeleteControls();
    if (iControllerInfos)
    {
        iControllerInfos->ResetAndDestroy();
    }
    delete iControllerInfos;

    if (iEventMonitor)
    {
        iEventMonitor->Cancel();
    }

    iController.Close();

    delete iEventMonitor;

    delete iFileName;
}


CMMAMMFPlayerBase::CMMAMMFPlayerBase(
    CMMAMMFResolver* aResolver) :
        iMediaTime(KTimeUnknown), iStartedEventTime(0)
{
    // implementation array ownership is transferred
    iControllerInfos = aResolver->ImplementationsOwnership();

    // content type ownership is transferred
    iContentType = aResolver->ContentTypeOwnership();

    // file name ownership is transferred
    iFileName = aResolver->FileNameOwnership();
}


void CMMAMMFPlayerBase::ConstructL()
{
    CMMAPlayer::ConstructL();

    // Create an event monitor
    iEventMonitor =
        CMMFControllerEventMonitor::NewL(*this, iController);
}


EXPORT_C RMMFController& CMMAMMFPlayerBase::Controller()
{
    return iController;
}

EXPORT_C TInt CMMAMMFPlayerBase::DoOpen(TUid aSourceUid,
                                        const TDesC8& aSourceData,
                                        TUid aSinkUid,
                                        const TDesC8& aSinkData,
                                        TMMFPrioritySettings aPrioritySettings)
{
    // Make sure any existing controller is closed.
    iEventMonitor->Cancel();
    iController.Close();

	// File type
	TInt fileType = ContentAccess::ENoDcf;
	
	// Determine file-type (9.2 onwards)
	#ifdef RD_JAVA_S60_RELEASE_9_2_ONWARDS
	if (iFileName)
	{
		ContentAccess::CContent* content = NULL;
		TRAPD(err, content = ContentAccess::CContent::NewL(
				  *iFileName, ContentAccess::EContentShareReadOnly));
		if (err)
		{
			DEBUG_INT("CMMAMMFPlayerBase::DoOpen: Trapped  err  = %d ", err);
		}
		else 
		{
			TInt value = 0;
			if (content->GetAttribute(ContentAccess::EFileType, value) == KErrNone)
			{
				fileType = value;
			}
			delete content ;
		}
	}
	#endif

    // Try opening and configuring each controller in turn
    TInt error = KErrNotSupported;
    TInt index = 0;

    // Try controllers until found a good controller or out of list
    while (index < iControllerInfos->Count())
    {
        #ifdef RD_JAVA_S60_RELEASE_9_2_ONWARDS
		// from 9.2 onwards we have overloaded API to open the controller in 
		// secure and non-secure process
		if (fileType != ContentAccess::ENoDcf)
		{
			DEBUG_INT("CMMAMMFPlayerBase::DoOpen: DRM ContentType = %d ", fileType);
			// Open the controller for OMA DRM file (v1 or v2)
			error = iController.OpenInSecureDRMProcess((*iControllerInfos)[ index ]->Uid(),
													   aPrioritySettings, ETrue);
		}
		else
		#endif

		{
			DEBUG("CMMAMMFPlayerBase::DoOpen: No DRM a Content");
			// Open the controller for non DRM file
			error = iController.Open((*iControllerInfos)[ index ]->Uid(),
									 aPrioritySettings, ETrue);
		}

		// If the controller was opened without error, start receiving events from it.
        if (!error)
        {
            iEventMonitor->Start();

            // Add the data source to the controller.
            error = iController.AddDataSource(aSourceUid, aSourceData);
        }

        // Add the data sink
        if (!error)
        {
            error = iController.AddDataSink(aSinkUid, aSinkData);
        }

        // Got a working controller -> done
        if (!error)
        {
            break;
        }

        // Close the non-working controller.
        iEventMonitor->Cancel();
        iController.Close();

        // Try the next one
        index++;

    }

    return error;
}

TBool CMMAMMFPlayerBase::IsFilePlayer()
{
    if (iFileName != NULL)
    {
        return ETrue;
    }
    return EFalse;
}

void CMMAMMFPlayerBase::StartL(TBool aPostEvent)
{
    iMediaTime = KTimeUnknown;
    User::LeaveIfError(iController.Play());
    if (aPostEvent)
    {
        // inform java side
        PostLongEvent(CMMAPlayerEvent::EStarted, iStartedEventTime);
    }
    
    ChangeState(EStarted);
    PostActionCompleted(KErrNone);   // java start return
}

void CMMAMMFPlayerBase::StopL(TBool aPostEvent)
{
    if (iState == EStarted)
    {
        TInt64 time;
        GetMediaTime(&time);
        iStartedEventTime = time;

        TInt err = KErrNone;
        DEBUG("CMMAMMFPlayerBase::StopL: Position not zero, pausing");
        err = iController.Pause();
        
        if ((err != KErrNone) && (err != KErrNotReady))
        {
            DEBUG_INT("CMMAMMFPlayerBase::StopL: pause/stop failed %d, leaving", err);
            User::Leave(err);
        }

        if (aPostEvent)
        {
            PostLongEvent(CMMAPlayerEvent::EStopped, time);
        }
        // go back to prefetched state
        ChangeState(EPrefetched);
    }
}


void CMMAMMFPlayerBase::DeallocateL()
{
    if (iState == EPrefetched)
    {
        // release all resources
        if (iEventMonitor)
        {
            iEventMonitor->Cancel();
        }

        // Change state first to enable AMMS to delete Effect API classes
        ChangeState(ERealized);
        iController.Stop();
        ResetSourceStreams();
    }
}


EXPORT_C void CMMAMMFPlayerBase::GetDuration(TInt64* aDuration)
{
    DEBUG("CMMAMMFPlayerBase::GetDuration ");
    if (iDuration == KTimeUnknown)
    {
        DEBUG("CMMAMMFPlayerBase::GetDuration Time unknown ");
        TTimeIntervalMicroSeconds duration;
        TInt err = iController.GetDuration(duration);
        if (!err)
        {
            iDuration = duration.Int64();
        }
    }
    *aDuration = iDuration;
    DEBUG("CMMAMMFPlayerBase::GetDuration - ");
}

void CMMAMMFPlayerBase::SetMediaTimeL(TInt64* aTime)
{
    DEBUG("CMMAMMFPlayerBase::SetMediaTimeL");

    // Negative values are not checked here
    // because it's done already in Java side.

    // Get clip duration
    TTimeIntervalMicroSeconds duration;
    User::LeaveIfError(iController.GetDuration(duration));
    DEBUG_INT("CMMAMMFPlayerBase::SetMediaTimeL iController.GetDuration=%d", duration.Int64());

    TTimeIntervalMicroSeconds position;

    // If the desired media time is beyond the duration,
    // the time is set to the end of the media.
    if (*aTime > duration.Int64())
    {
        position = duration;
    }
    else
    {
        position = *aTime;
    }

    TBool paused = EFalse;
    TInt err = KErrNone;

    if (iState == EStarted)
    {
        paused = ETrue;
        User::LeaveIfError(err = iController.Pause());
        DEBUG_INT("CMMAMMFPlayerBase::SetMediaTimeL after iController.Pause = %d", err);
    }

    if (err == KErrNone)
    {
        // The controller must be in the PRIMED or PLAYING state
        User::LeaveIfError(err = iController.SetPosition(position));
        DEBUG_INT("CMMAMMFPlayerBase::SetMediaTimeL iController.SetPosition() = %d", err);
    }

    // Reset cached media time, because actual set position may be
    // something else than aTime.
    iMediaTime = KTimeUnknown;

    // Inform about the position change to the StateListeners
    ChangeState(iState);

    // Get the actual media time
    GetMediaTime(aTime);

    iStartedEventTime = iMediaTime;

    if (err == KErrNone)
    {
        if (paused == (TBool)ETrue)
        {
            User::LeaveIfError(err = iController.Play());
            DEBUG_INT("CMMAMMFPlayerBase::SetMediaTimeL iController.Play() = %d", err);
        }
    }

    if (err != KErrNone)
    {
        User::Leave(err);
    }
}

void CMMAMMFPlayerBase::GetMediaTime(TInt64* aMediaTime)
{
    DEBUG("CMMAMMFPlayerBase::GetMediaTime +");
    TTimeIntervalMicroSeconds position(0);

    if (iMediaTime == KTimeUnknown || iState == EStarted)
    {
        // The controller must be in the PRIMED or PLAYING state
        TInt error(iController.GetPosition(position));

        if (error == KErrNone)
        {
            TInt64 newTime = position.Int64();
            DEBUG_INT("CMMAMMFPlayerBase::GetMediaTime iController.GetPosition : %d", newTime);

            // Sanity check for media time going backwards or beyond the
            // duration.
            // Some native controls may return zero media time for
            // a few moments just before playback will complete.
            if (newTime < iMediaTime ||
                    (iDuration > 0 && newTime > iDuration))
            {
                DEBUG("CMMAMMFPlayerBase::GetMediaTime.GetDuration ");
                GetDuration(&iMediaTime);
            }
            else
            {
                DEBUG("CMMAMMFPlayerBase::GetMediaTime.else ");
                // set return value
                iMediaTime = newTime;
            }
        }
        else
        {
            DEBUG_INT("CMMAMMFPlayerBase::GetMediaTime: error=%d, returning TIME_UNKNOWN", error);
            // cannot get media time
            iMediaTime = KTimeUnknown;
        }
    }
    *aMediaTime = iMediaTime;
    DEBUG_INT("CMMAMMFPlayerBase::GetMediaTime - %d", *aMediaTime);
}

void CMMAMMFPlayerBase::CloseL()
{
    CMMAPlayer::CloseL();
    if (iEventMonitor)
    {
        iEventMonitor->Cancel();
    }
    // First delete the control and then close the controller
    // Added after AudioOutputControl
    iController.Stop();
    delete iEventMonitor;
    iEventMonitor = NULL;
}

void CMMAMMFPlayerBase::HandleEvent(const TMMFEvent& /*aEvent*/)
{
    // empty implementation
}

//  END OF FILE
