#
# Copyright (c) 2009 Nokia Corporation and/or its subsidiary(-ies).
# All rights reserved.
# This component and the accompanying materials are made available
# under the terms of "Eclipse Public License v1.0"
# which accompanies this distribution, and is available
# at the URL "http://www.eclipse.org/legal/epl-v10.html".
#
# Initial Contributors:
# Nokia Corporation - initial contribution.
#
# Contributors:
#
# Description: 
#

TEMPLATE=lib
TARGET=javam2g
CONFIG += omj java stl
CONFIG -= qt

SOURCES += ../src/*.cpp
SOURCES += ../src/jni/*.cpp

LIBS += -lavkon \
        -lbitgdi \
        -lcharconv \
        -lcone \
        -leikcore \
        -leswt \
        -lfbscli \
        -lgdi \
        -ljavalegacyutils \
        -lSVGEngineJI

include(../../../build/omj.pri)