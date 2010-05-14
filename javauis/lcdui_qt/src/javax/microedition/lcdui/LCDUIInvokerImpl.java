/*
* Copyright (c) 2009 Nokia Corporation and/or its subsidiary(-ies).
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
package javax.microedition.lcdui;

import org.eclipse.swt.graphics.Internal_GfxPackageSupport;
import org.eclipse.swt.internal.qt.graphics.GraphicsContext;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Button;
import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mj.impl.nokialcdui.LCDUIInvoker;
import com.nokia.mj.impl.nokialcdui.ItemControlStateChangeListener;

final class LCDUIInvokerImpl extends LCDUIInvoker
{

    private boolean eswtReturn;

    /**
      * Constant for Soft Key Label Height
      */
    public static final int LABEL_HEIGHT = 30;

    static void createInvoker()
    {
        LCDUIInvoker.setInvoker(
            new javax.microedition.lcdui.LCDUIInvokerImpl());
    }


    protected void doESWTUIThreadRunnerSyncExec(Runnable runnable)
    {
        ESWTUIThreadRunner.syncExec(runnable);
    }

    /**
     * Returns main eSWT Control that is used by LCDUI Displayable or Item.
     *
     * @param lcduiObject - LCDUI Displayable or Item.
     * @return eSWT Control that is used by Displayable/Item or null if no
     *         Control is available.
     */
    protected Control doGetEswtControl(Object lcduiObject)
    {
        Logger.method(this, "doGetEswtControl", lcduiObject);
        if(lcduiObject instanceof Displayable)
        {
            return ((Displayable) lcduiObject).getShell();
        }
        else if(lcduiObject instanceof Item)
        {
            Item item = (Item) lcduiObject;
            if(item.isContainedInForm())
            {
                Form parentForm = (Form) item.getParent();
                return parentForm.getLayoutPolicy().getItemControl(item);
            }
        }
        return null;
    }


    protected void doSetItemControlStateChangeListener(ItemControlStateChangeListener listener,Item item)
    {
        Logger.method(this, "doItemStateChangeListener",listener);
        item.setItemControlStateChangeListener(listener);
    }

    /*
    * This method checks if the current Displayable exists
    * If yes, then the current Displayable is asked for the property
    * If no, the null is returned
    */
    protected String getSoftKeyLabelLocationImpl(String softKeyId)
    {
        String SoftKeyLabelLocation = null;

        Displayable disp = javax.microedition.lcdui.Display.getDisplay().getCurrent();

        if((disp != null) && (disp instanceof Canvas))
        {
            if(((Canvas) disp).IsFullScreenMode())
            {

                if("com.nokia.softkey1.label.location".equals(softKeyId))
                {
                    SoftKeyLabelLocation = "0," + String.valueOf(((Canvas) disp).getHeight()) + ","
                                           + String.valueOf((((Canvas) disp).getWidth())/2) + ","
                                           + String.valueOf(LABEL_HEIGHT);
                }
                else if("com.nokia.softkey2.label.location".equals(softKeyId))
                {
                    SoftKeyLabelLocation = String.valueOf((((Canvas) disp).getWidth())/2) + ","
                                           + String.valueOf(((Canvas) disp).getHeight()) + ","
                                           + String.valueOf((((Canvas) disp).getWidth())/2) + ","
                                           + String.valueOf(LABEL_HEIGHT);
                }
            }
        }

        return SoftKeyLabelLocation;
    }

    /*
    * This method checks if the current Displayable exists
    * If yes, then the current Displayable is asked for the property
    * If no, the null is returned
    */
    protected String getSoftKeyLabelAnchorImpl(String softKeyId)
    {
        String SoftKeyLabelAnchor = null;

        Displayable disp = javax.microedition.lcdui.Display.getDisplay().getCurrent();

        if((disp != null) && (disp instanceof Canvas))
        {
            if(null == ((Canvas) disp).getCanvasKeypad())
            {
                SoftKeyLabelAnchor = String.valueOf(Graphics.TOP | Graphics.HCENTER);
            }
            else
            {
                if("com.nokia.softkey1.label.anchor".equals(softKeyId))
                {
                    SoftKeyLabelAnchor = String.valueOf(Graphics.TOP | Graphics.LEFT);
                }
                else if("com.nokia.softkey2.label.anchor".equals(softKeyId))
                {
                    SoftKeyLabelAnchor = String.valueOf(Graphics.TOP | Graphics.RIGHT);
                }
            }
        }

        return SoftKeyLabelAnchor;
    }

    protected Display doGetEswtDisplay()
    {
        Logger.method(this, "doGetEswtDisplay");
        return ESWTUIThreadRunner.getInstance().getDisplay();
    }

    protected org.eclipse.swt.graphics.Image doGetEswtImage(Image img)
    {
        Logger.method(this, "doGetEswtImage", img);
        return Image.getESWTImage(img);
    }

    protected Image doGetLcduiImage(org.eclipse.swt.graphics.Image img)
    {
        Logger.method(this, "doGetLcduiImage", img);
        return new Image(img, true);
    }

    protected DirectGraphics doGetDirectGraphics(Graphics g)
    {
        Logger.method(this, "doGetDirectGraphics", g);
        if(g != null)
        {
            return g.getDirectGraphics();
        }
        return null;
    }

    protected GraphicsContext doGetGc(Graphics g)
    {
        Logger.method(this, "doGetGc", g);
        if(g != null)
        {
            return g.getGc();
        }
        return null;
    }

    protected Font doGetFreeSizedFont(int face, int style, int height)
    {
        Logger.method(this, "doGetFreeSizedFont");
        return Font.getFreeSizedFont(face, style, height);
    }


    protected void doInitGameCanvas(Object canvas, boolean suppressKeys)
    {
        Logger.method(canvas, "doInitGameCanvas", String.valueOf(suppressKeys));
        ((Canvas) canvas).initGameCanvas(suppressKeys);
    }

    protected Object doGetGraphics(Object canvas)
    {
        return ((Canvas) canvas).getGameBufferGraphics();
    }

    protected Object doGetFlushLock(Object graphics)
    {
        return ((Graphics) graphics).getLock();
    }

    protected int doGetKeyStates(Object canvas)
    {
        return ((Canvas) canvas).getGameKeyStates();
    }

    protected void doRenderGraphics(Object canvas, Object graphics)
    {
        ((Canvas) canvas).renderGraphics((Graphics) graphics);
    }

    protected void doFlushGraphics(Object canvas,
                                   int x, int y, int width, int height)
    {
        ((Canvas) canvas).flushGameBuffer(x, y, width, height);
    }

    protected String doGetDynamicProperty(String key)
    {
        if("com.nokia.key.scancode".equals(key))
        {
            return String.valueOf(ESWTUIThreadRunner.getLastKeyScancode());
        }
        else if("com.nokia.key.modifier".equals(key))
        {
            return String.valueOf(ESWTUIThreadRunner.getLastKeyModifier());
        }
        else if("com.nokia.keyboard.type".equals(key))
        {
            // TODO: eSWT support required
            /*
            No keypad (for example a touch device without keypad)   - None
            Standard ITU-T keypad (Phone keypad with 12 keys)       - PhoneKeypad
            QWERTY (4x12 layout)                                    - FullKeyboard
            QWERTY (limited, 4x10 layout)                           - LimitedKeyboard4x10
            QWERTY (limited, 3x11 layout)                           - LimitedKeyboard3x11
            Half QWERTY layout (aka Compact QWERTY keyboard).       - HalfKeyboard
            Custom QWERTY layout                                    - Custom
            Unknown layout                                          - Unknown
            */
            return "Unknown";
        }
        else if("com.nokia.softkey1.label.location".equals(key))
        {
            return getSoftKeyLabelLocationImpl(key);
        }
        else if("com.nokia.softkey1.label.anchor".equals(key))
        {
            return getSoftKeyLabelAnchorImpl(key);
        }
        else if("com.nokia.softkey2.label.location".equals(key))
        {
            return getSoftKeyLabelLocationImpl(key);
        }
        else if("com.nokia.softkey2.label.anchor".equals(key))
        {
            return getSoftKeyLabelAnchorImpl(key);
        }
        return "key not supported";
    }

    protected boolean doDetectCollision(Image image1, int transform1, int p1x, int p1y,
                                        int r1x1, int r1y1, int r1x2, int r1y2,
                                        Image image2, int transform2, int p2x, int p2y,
                                        int r2x1, int r2y1, int r2x2, int r2y2)
    {

        final org.eclipse.swt.graphics.Image eswtImg1 = Image.getESWTImage(image1);;
        final int ftransform1 = transform1;
        final int fp1x = p1x;
        final int fp1y = p1y;
        final int fr1x1 = r1x1;
        final int fr1y1 = r1y1;
        final int fr1x2 = r1x2;
        final int fr1y2 = r1y2;

        final org.eclipse.swt.graphics.Image eswtImg2 = Image.getESWTImage(image2);
        final int ftransform2 = transform2;
        final int fp2x = p2x;
        final int fp2y = p2y;
        final int fr2x1 = r2x1;
        final int fr2y1 = r2y1;
        final int fr2x2 = r2x2;
        final int fr2y2 = r2y2;

        eswtReturn = false;
        ESWTUIThreadRunner.safeSyncExec(new Runnable()
        {
            public void run()
            {
                eswtReturn = org.eclipse.swt.internal.qt.graphics.Image.detectCollision(
                                 Internal_GfxPackageSupport.getImage(eswtImg1), ftransform1, fp1x, fp1y,
                                 fr1x1, fr1y1, fr1x2, fr1y2,
                                 Internal_GfxPackageSupport.getImage(eswtImg2), ftransform2, fp2x, fp2y,
                                 fr2x1, fr2y1, fr2x2, fr2y2);
            }
        });
        return eswtReturn;
    }

}