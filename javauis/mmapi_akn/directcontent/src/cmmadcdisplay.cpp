/*
* Copyright (c) 2002-2009 Nokia Corporation and/or its subsidiary(-ies).
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
* Description:
*
*/


//  Include Files
#include <e32def.h>
#include <fbs.h>
#include <jdebug.h>

#include "cmmaplayerevent.h"
#include "mmmaguiplayer.h"
#include "mmmadisplaywindow.h"
#include "cmmadcdisplay.h"
#include "mmmacontainer.h"
#include "cmmaeventsource.h"
#include "cmmadcrepaintevent.h"
#include "cmmadcfullscreenevent.h"
#include "cmmadcinvalidateevent.h"

// CONSTRUCTION
// Static constructor, leaves pointer to cleanup-stack
CMMADCDisplay* CMMADCDisplay::NewLC(MMMAGuiPlayer* aPlayer,
                                    CMMAEventSource* aEventSource,
                                    jobject aGUIObject)
{
    CMMADCDisplay* self =
        new(ELeave) CMMADCDisplay(aPlayer, aEventSource, aGUIObject);
    CleanupStack::PushL(self);
    self->iRepaint = new(ELeave) CMMADCRepaintEvent(aGUIObject);
    return self;
}

// Destructor (virtual by CBase)
CMMADCDisplay::~CMMADCDisplay()
{
    if (iContainer)
    {
        iContainer->MdcRemoveContent();
    }
    delete iBitmap;
    delete iRepaint;
}

// interface MMMADisplay
void CMMADCDisplay::DrawFrameL(const CFbsBitmap* aBitmap)
{
    // This method is called only if bitmap is used.
    TInt err = InitBitmapMode();

    if (iVisible && iWindow &&
            iContainer && iContainer->MdcContainerVisibility() &&
            err == KErrNone)
    {
        iWindow->DrawFrameL(aBitmap);

        // container will draw bitmap obtained with MdcFrameBuffer method
        if (!iRepaint->IsActive())
        {
            iRepaint->SetActive();
            iEventSource->PostEvent(iRepaint, CJavaEventBase::EEventPriority);
        }
    }
}

// interface MMMADisplay
void CMMADCDisplay::SetDisplaySizeL(const TSize& aSize)
{
    DEBUG_INT2("CMMADCDisplay::SetDisplaySizeL w %d h %d",
               aSize.iWidth, aSize.iHeight);
    // user rect contains size set from java.
    iUserRect.SetSize(aSize);

    if (iContainer)
    {
        CMMADCInvalidateEvent* event =
            new(ELeave)CMMADCInvalidateEvent(iGUIObject, aSize);
        iEventSource->PostEvent(event, CJavaEventBase::EEventPriority);
    }
}

// interface MMMADisplay
void CMMADCDisplay::SetDisplayLocationL(const TPoint& /*aPosition*/)
{
    DEBUG("CMMADCDisplay::SetDisplayLocationL");
    // This method only works when the USE_DIRECT_VIDEO mode is set.
    // In USE_GUI_PRIMITIVE mode, this call will be ignored.
}

// interface MMMADisplay
TPoint CMMADCDisplay::DisplayLocation()
{
    DEBUG("CMMADCDisplay::DisplayLocation");
    // This method returns always (0,0),
    // because SetDisplayLocationL call is ignored.
    return TPoint(0, 0);
}

// interface MMMADisplay
void CMMADCDisplay::SetFullScreenL(TBool aFullScreen)
{
    DEBUG_INT("CMMADCDisplay::SetFullScreenL %d", aFullScreen);
    // This method tries to set eSWT Widget size to its parent size.
    // If real full screen mode is needed parent Composite must be in
    // fullscreen mode (for example with MobileShell's setFullScreenMode method).
    if (iContainer)
    {
        CMMADCFullScreenEvent* event =
            new(ELeave)CMMADCFullScreenEvent(iGUIObject, aFullScreen);
        iEventSource->PostEvent(event, CJavaEventBase::EEventPriority);
    }
}

// interface MMMADisplay
void CMMADCDisplay::SourceSizeChanged(const TSize& aSourceSize)
{
    DEBUG_INT2("CMMADCDisplay::SourceSizeChanged %d %d",
               aSourceSize.iWidth,
               aSourceSize.iHeight);

#ifdef RD_JAVA_NGA_ENABLED
    if (iWindow)
    {
        TPoint topLeft(0, 0);
        iWindow->SetVideoCropRegion(TRect(topLeft,aSourceSize));
    }
#endif

    if(IsUserRectSet() || iFullScreen)
    {
       return;
    }
    if (iWindow)
    {
        TRect tmp(TPoint(0, 0), aSourceSize);
        iWindow->SetDrawRect(tmp);
        iWindow->SetWindowRect(tmp,MMMADisplay::EMmaThread);

        // set visibility without using event server because this is called
        // from MMA thread
        if (iContainer)
        {
            if (iContainer->MdcContainerVisibility())
            {
                iWindow->SetVisible(ETrue, EFalse);
            }
        }
    }
}

// interface MMMADisplay
TBool CMMADCDisplay::IsVisible()
{
    TBool visible = EFalse;
    if (iContainer)
    {
        visible = iContainer->MdcContainerVisibility();
    }
    // else invisible

    // return true if both are visible
    return iVisible && visible;
}


// interface MMMADisplay
TBool CMMADCDisplay::HasContainer()
{
    return iContainer != NULL;
}


// interface MMMADirectContent
void CMMADCDisplay::MdcContainerVisibilityChanged(TBool aVisible)
{
    DEBUG_INT("CMMADCDisplay::MdcContainerVisibilityChanged aVisible %d",
              aVisible);
    if (iWindow)
    {
        iWindow->SetVisible(aVisible && iVisible);
    }
    DEBUG("CMMADCDisplay::MdcContainerVisibilityChanged OK");
}

// interface MMMADirectContent
void CMMADCDisplay::MdcContentRectChanged(const TRect& aContentRect,
        const TRect& aParentRect)
{
    DEBUG("MMA::CMMADCDisplay::MdcContentRectChanged");
    if (iWindow)
    {
        TSize size = aContentRect.Size();
        iWindow->SetDrawRectThread(TRect(size));
        TInt err = iEventSource->ExecuteTrap(CMMADCDisplay::SetDrawRectL,
                                             this,
                                             &size);
        if (err == KErrNone)
        {
            // bitmap window ignores window rect and position
            iWindow->SetWindowRect(aParentRect,MMMADisplay::EUiThread);
            iWindow->SetPosition(aContentRect.iTl - aParentRect.iTl);
        }
    }
}

void CMMADCDisplay::MdcContainerWindowRectChanged(const TRect&
#ifdef RD_JAVA_NGA_ENABLED
        aRect
#endif
                                                 )
{
    DEBUG("CMMADCDisplay::MdcContainerWindowRectChanged");

#ifdef RD_JAVA_NGA_ENABLED
    if (iWindow)
    {
        iWindow->SetRWindowRect(aRect, MMMADisplay::EUiThread);
    }
#endif
}

// interface MMMADirectContent
void CMMADCDisplay::MdcContainerDestroyed()
{
    DEBUG("MMA::CMMADCDisplay::MdcContainerDestroyed");
    if (iContainer)
    {
        iContainer->MdcRemoveContent();
    }

    iContainer = NULL;
    if (iWindow)
    {
        iWindow->SetVisible(EFalse);
        iWindow->ContainerDestroyed();
    }
}

// interface MMMADirectContent
void CMMADCDisplay::MdcSetContainer(MMMAContainer* aContainer)
{
    iContainer = aContainer;
    TSize sourceSize = iPlayer->SourceSize();
    DEBUG_INT2("CMMADCDisplay::MdcSetContainer source size %d %d",
               sourceSize.iWidth, sourceSize.iHeight);
    aContainer->MdcInvalidate(sourceSize);
    if (iWindow)
    {
        // Notify window that container has been set
        iWindow->ContainerSet();

        TRect controlRect;
        TRect parentRect;
        iContainer->MdcGetContentRect(controlRect, parentRect);

        // bitmap window ignores window rect and position
        iWindow->SetWindowRect(parentRect,MMMADisplay::EUiThread);
        iWindow->SetPosition(controlRect.iTl - parentRect.iTl);

        DEBUG_INT("CMMADCDisplay::MdcSetContainer container visible %d",
                  aContainer->MdcContainerVisibility());
        DEBUG_INT("CMMADCDisplay::MdcSetContainer content visible %d",
                  iVisible);

        iWindow->SetVisible(aContainer->MdcContainerVisibility() &&
                            iVisible);
    }

    if(iFixUIOrientation)
    {
        iContainer->MdcFixUIOrientation(ETrue);
        iFixUIOrientation = EFalse;
    }
}

// interface MMMADirectContent
CFbsBitmap* CMMADCDisplay::MdcFrameBuffer() const
{
    return iBitmap;
}

// interface MMMADirectContent
TSize CMMADCDisplay::MdcSourceSize()
{
    return iPlayer->SourceSize();
}

void CMMADCDisplay::MdcContentBoundsChanged(const TRect& /*aRect*/)
{
}

void CMMADCDisplay::UIGetDSAResources(
    MUiEventConsumer& aConsumer,
    MMMADisplay::TThreadType aThreadType)
{
    if (iContainer)
    {
        iContainer->MdcGetDSAResources(aConsumer, aThreadType);
    }
}

void CMMADCDisplay::UIGetCallback(
    MUiEventConsumer& aConsumer,
    TInt aCallbackId)
{
    if (iContainer)
    {
        iContainer->MdcGetUICallback(aConsumer, aCallbackId);
    }
}

void CMMADCDisplay::FixUIOrientation(TBool aFix)
{
    if (iContainer)
    {
        iContainer->MdcFixUIOrientation(aFix);
    }
    else
    {
        // fix orientation when container becomes available
        iFixUIOrientation = aFix;
    }
}

TInt CMMADCDisplay::InitBitmapMode()
{
    TInt errCode = KErrNone;
    // If there is no bitmap, create one and set it to window
    if (!iBitmap && iWindow)
    {
        iBitmap = new CFbsBitmap;
        if (iBitmap)
        {
            errCode = iBitmap->Create(iPlayer->SourceSize(),
                                      EColor16MA);
        }
        else
        {
            errCode = KErrNoMemory;
        }

        if (errCode == KErrNone)
        {
            TRAP(errCode,
                 iWindow->SetDestinationBitmapL(iBitmap));
        }
    }
    return errCode;
}

void CMMADCDisplay::SetDrawRectL(CMMADCDisplay* aDisplay, TSize* aSize)
{
    if (aDisplay->iBitmap)
    {
        User::LeaveIfError(aDisplay->iBitmap->Resize(*aSize));
        aDisplay->iWindow->SetDestinationBitmapL(aDisplay->iBitmap);
    }
}

CMMADCDisplay::CMMADCDisplay(MMMAGuiPlayer* aPlayer,
                             CMMAEventSource* aEventSource,
                             jobject aGUIObject):
                             iFixUIOrientation(EFalse)
{
    iPlayer = aPlayer;
    iEventSource = aEventSource;
    iGUIObject = aGUIObject;
    // GUI_PRIMITIVE is visible by default.
    iVisible = ETrue;
}

// END OF FILE