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
package com.nokia.mj.test.lapi;

import j2meunit.framework.*;
import javax.microedition.location.*;

import java.util.Enumeration;
import java.util.Vector;

/**
 * <b>TEST CASE SPECIFICATION</b>
 *
 * These test are for testing error reports, so that we can avoid regressions
 * Used for complicated tests that don't fit in the other test suites
 *
 */

public class ErrorsTest extends TestCase
{

    private static final String STORE_1_NAME = "store1";

    private static final String STORE_2_NAME = "store2";

    private static final String LONG_CATEGORY_NAME = "Relatively long category name...";

    private static final String SHORT_CATEGORY_NAME = "a";

    private static final String LANDMARKSTORE_NAME = "store1";

    private static final String SEQUENTIAL_CATEGORY = "sequential_category_tck_jsr179";

    private static final QualifiedCoordinates VALID_QUALIFIED_COORDINATES = new QualifiedCoordinates(
        45.0d, 120.0d, 274f, 7.2f, 10.5f);

    public ErrorsTest()
    {
    }

    public ErrorsTest(String sTestName, TestMethod rTestMethod)
    {
        super(sTestName, rTestMethod);
    }

    /***************************************************************************
     * Creates the test suite. You need to add a new aSuite.addTest antry for
     * any new test methods, otherwise they won't be run.
     */
    public Test suite()
    {

        TestSuite aSuite = new TestSuite();

        /*
         * aSuite.addTest(new ErrorsTest("testGc1", new TestMethod() { public
         * void run(TestCase tc) {((ErrorsTest) tc).testGc1(); } }));
         */

        aSuite.addTest(new ErrorsTest("testGc2", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).testGc2();
            }
        }));

        aSuite.addTest(new ErrorsTest("tp17", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).tp17();
            }
        }));

        aSuite.addTest(new ErrorsTest("tp19_p1", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).tp19_p1();
            }
        }));

        aSuite.addTest(new ErrorsTest("tp19_p2", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).tp19_p2();
            }
        }));

        aSuite.addTest(new ErrorsTest("tp19_p3", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).tp19_p3();
            }
        }));

        aSuite.addTest(new ErrorsTest("tp19_p4", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).tp19_p4();
            }
        }));

        aSuite.addTest(new ErrorsTest("tp19_p5", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).tp19_p5();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore0412", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore0412();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore0608", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore0608();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore0701", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore0701();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore0702", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore0702();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore0703", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore0703();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore0801", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore0801();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore0802", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore0802();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore0803", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore0803();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore1201", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore1201();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore1202", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore1202();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore1303", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore1303();
            }
        }));

        aSuite.addTest(new ErrorsTest("landmarkStore1701", new TestMethod()
        {
            public void run(TestCase tc)
            {
                ((ErrorsTest) tc).landmarkStore1701();
            }
        }));

        return aSuite;

    }

    private void assertContinue(String aReason, boolean aCond)
    {
        if (!aCond)
            assertTrue(aReason, false);
    }

    public void testGc1()
    {

        try
        {
            LandmarkStore store = LandmarkStore.getInstance(null);
            String id = store.toString();
            assertContinue("Not the same instance", store == LandmarkStore
                           .getInstance(null));

            store = null;
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }

            store = LandmarkStore.getInstance(null);
            assertContinue("The same instance", !id.equals(store.toString()));

            Landmark lm = new Landmark("test", null, null, null);
            store.addLandmark(lm, null);
            store.deleteLandmark(lm);

            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    private void testGc_helper1()
    {
        try
        {
            LandmarkStore store = LandmarkStore.getInstance(null);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    private void testGc_helper2()
    {
        try
        {
            System.gc();
            LandmarkStore store = LandmarkStore.getInstance(null);

            Landmark lm = new Landmark("test", null, null, null);
            store.addLandmark(lm, null);
            store.deleteLandmark(lm);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    public void testGc2()
    {
        try
        {
            testGc_helper1();
            testGc_helper2();

            deleteAllLandmarksAndCategories();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    public void tp17()
    {

        try
        {
            // 1.
            // ------------------------------------------------------------------
            String lm1_name1 = "lm1_name";
            String lm1_description1 = "lm1_description";
            String lm1_street1 = "lm1_street";
            double lm1_latitude1 = 12.3;
            double lm1_longitude1 = 32.1;
            float lm1_altitude1 = 3.9f;
            float lm1_horAccuracy1 = 5.5f;
            float lm1_verAccuracy1 = 6.6f;

            QualifiedCoordinates lm1_coords1 = new QualifiedCoordinates(
                lm1_latitude1, lm1_longitude1, lm1_altitude1,
                lm1_horAccuracy1, lm1_verAccuracy1);

            AddressInfo lm1_addressInfo1 = new AddressInfo();
            lm1_addressInfo1.setField(AddressInfo.STREET, lm1_street1);

            Landmark lm1 = new Landmark(lm1_name1, lm1_description1,
                                        lm1_coords1, lm1_addressInfo1);

            QualifiedCoordinates lm1_coords_returned1 = lm1
                    .getQualifiedCoordinates();

            AddressInfo lm1_addressInfo_returned1 = lm1.getAddressInfo();

            // 2.
            // ------------------------------------------------------------------
            String lm1_name2 = "lm1_name2";
            String lm1_description2 = "lm1_description2";
            String lm1_street2 = "lm1_street2";
            double lm1_latitude2 = 16.8;
            double lm1_longitude2 = 26.7;
            float lm1_altitude2 = 7.1f;
            float lm1_horAccuracy2 = 2.7f;
            float lm1_verAccuracy2 = 8.1f;

            QualifiedCoordinates lm1_coords2 = new QualifiedCoordinates(
                lm1_latitude2, lm1_longitude2, lm1_altitude2,
                lm1_horAccuracy2, lm1_verAccuracy2);

            AddressInfo lm1_addressInfo2 = new AddressInfo();
            lm1_addressInfo2.setField(AddressInfo.STREET, lm1_street2);

            lm1.setName(lm1_name2);
            lm1.setDescription(lm1_description2);
            lm1.setQualifiedCoordinates(lm1_coords2);
            lm1.setAddressInfo(lm1_addressInfo2);

            QualifiedCoordinates lm1_coords_returned2 = lm1
                    .getQualifiedCoordinates();

            AddressInfo lm1_addressInfo_returned2 = lm1.getAddressInfo();

            // 3.
            // ------------------------------------------------------------------
            boolean check3 = false;

            try
            {
                lm1.setName(null);
                assertContinue(
                    "TP17-ERR stage3, NullPointerException was not thrown as expected.",
                    false);
            }
            catch (java.lang.NullPointerException e)
            {
                check3 = true;
            }

            // 4.
            // ------------------------------------------------------------------
            String lm2_name1 = "lm2_name1";

            Landmark lm2 = new Landmark(lm2_name1, null, null, null);

            // 5.
            // ------------------------------------------------------------------
            boolean check5 = false;

            try
            {
                Landmark lm3 = new Landmark(null, null, null, null);
                assertContinue(
                    "TP17-ERR stage5, NullPointerException was not thrown as expected.",
                    false);
            }
            catch (java.lang.NullPointerException e)
            {
                check5 = true;
            }
            assertTrue("", true);
        }
        catch (Throwable e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    public void tp19_p1()
    {

        try
        {
            System.gc();
            // waitForEposShutdown(10000);
            // waitForLandmarksShutdown(10000);
            // delete_EPOSLM_LDB();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            deleteAllLandmarksAndCategories();

            LandmarkStore store = LandmarkStore.getInstance(null);

            // 1.
            // ------------------------------------------------------------------

            final String tc80Category = "TC80CategoryISINDEED32CHARACTERS";

            store.addCategory(tc80Category);

            // 2.
            // ------------------------------------------------------------------

            boolean check = false;

            try
            {
                store.addCategory(tc80Category);
                assertContinue(
                    "TP19-ERR TC80 stage2, IllegalArgumentException was not thrown as expected.",
                    false);
            }
            catch (java.lang.IllegalArgumentException e)
            {
                check = true;
            }

            // 3.
            // ------------------------------------------------------------------

            check = false;

            try
            {
                store.addCategory(null);
                assertContinue(
                    "TP19-ERR TC80 stage3, NullPointerException was not thrown as expected.",
                    false);
            }
            catch (java.lang.NullPointerException e)
            {
                check = true;
            }
            store = null;
            assertTrue("", true);
        }
        catch (Throwable e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    public void tp19_p2()
    {
        try
        {
            System.gc();
            // waitForEposShutdown(10000);
            // waitForLandmarksShutdown(10000);
            // delete_EPOSLM_LDB();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            deleteAllLandmarksAndCategories();

            LandmarkStore store = LandmarkStore.getInstance(null);

            // 1.
            // ------------------------------------------------------------------

            Enumeration emptyEnum = store.getCategories();
            assertContinue("TP19-ERR TC90 stage1, Enumeration was not empty.",
                           !emptyEnum.hasMoreElements());

            // 2.
            // ------------------------------------------------------------------

            final String tc90cat1 = "TC90cat1";
            final String tc90cat2 = "TC90cat2";

            store.addCategory(tc90cat1);
            store.addCategory(tc90cat2);

            // 3.
            // ------------------------------------------------------------------

            Enumeration categories = store.getCategories();
            assertContinue("TP19-ERR stage3, Enumeration was empty.",
                           categories.hasMoreElements());

            boolean tc90cat1_found = false;
            boolean tc90cat2_found = false;

            int count = 0;

            while (categories.hasMoreElements())
            {

                String cat_temporary = (String) categories.nextElement();

                assertContinue(
                    "TP19-ERR stage3, An unexpected Category was found in the LandmarkStore.",
                    tc90cat1.equals(cat_temporary)
                    || tc90cat2.equals(cat_temporary));

                if (tc90cat1.equals(cat_temporary))
                {
                    tc90cat1_found = true;
                }
                if (tc90cat2.equals(cat_temporary))
                {
                    tc90cat2_found = true;
                }

                count++;
            }

            assertContinue(
                "TP19-ERR stage3, There were not 2 elements in the Enumeration.",
                count == 2);
            assertContinue(
                "TP19-ERR stage3, Category nr 1 - tc90cat1 - was not found in the Landmark store.",
                tc90cat1_found);
            assertContinue(
                "TP19-ERR stage3, Category nr 2 - tc90cat2 - was not found in the Landmark store.",
                tc90cat2_found);
            assertTrue("", true);

        }
        catch (Throwable e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    public void tp19_p3()
    {
        try
        {
            System.gc();
            // waitForEposShutdown(10000);
            // waitForLandmarksShutdown(10000);
            // delete_EPOSLM_LDB();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            deleteAllLandmarksAndCategories();

            LandmarkStore store = LandmarkStore.getInstance(null);

            final String tc91cat1 = "TC91cat1";
            store.addCategory(tc91cat1);

            // 1.
            // ------------------------------------------------------------------

            store.deleteCategory(tc91cat1);

            // 2.
            // ------------------------------------------------------------------

            boolean check = false;

            try
            {
                store.deleteCategory(null);
                assertContinue(
                    "TP19-ERR stage4, NullPointerException was not thrown as expected.",
                    false);
            }
            catch (java.lang.NullPointerException e)
            {
                check = true;
            }
            assertContinue("TP19-ERR TC91 stage2, Wrong Throwable was thrown.",
                           check);

            // 3.
            // ------------------------------------------------------------------

            store.deleteCategory("TC91_ShouldReturnSilently");
            assertTrue("", true);
        }
        catch (Throwable e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    public void tp19_p4()
    {
        try
        {
            System.gc();
            // waitForEposShutdown(10000);
            // waitForLandmarksShutdown(10000);
            // delete_EPOSLM_LDB();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            deleteAllLandmarksAndCategories();

            LandmarkStore store = LandmarkStore.getInstance(null);

            final String tc92cat = "tc92cat";
            final String tc92lmstring = "tc92lm";

            Landmark tc92lm = new Landmark(tc92lmstring, null, null, null);
            store.addLandmark(tc92lm, null);

            // int err = iVTH.addLandmarksToCategory(tc92lmstring, tc92cat);
            // assert(err == 0, "TP19-ERR TC92 pre-condition error: (1) Landmark
            // was not sucessfully added to category.");
            // assert(iVTH.isLandmarkInCategory(tc92lmstring, tc92cat),
            // "TP19-ERR TC92 pre-condition error: (2) Landmark was not
            // sucessfully added to category.");
            store.addCategory(tc92cat);
            store.addLandmark(tc92lm, tc92cat);

            // Flow:
            // ---------------------------------------------------------------
            store.deleteCategory(tc92cat);
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    public void tp19_p5()
    {
        try
        {
            System.gc();
            // waitForEposShutdown(10000);
            // waitForLandmarksShutdown(10000);
            // delete_EPOSLM_LDB();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            deleteAllLandmarksAndCategories();

            LandmarkStore store = LandmarkStore.getInstance(null);

            final String tc93cat1 = "tc93cat1";
            final String tc93cat2 = "tc93cat2";
            final String tc93lmstring = "tc93lm";

            // iVTH.addCategory(tc93cat1);
            // assert(iVTH.categoryExists(tc93cat1), "TP19-ERR TC93
            // pre-condition error: category tc93cat1 was not added to
            // LandmarkStore.");
            store.addCategory(tc93cat1);

            // iVTH.addCategory(tc93cat2);
            // assert(iVTH.categoryExists(tc93cat2), "TP19-ERR TC93
            // pre-condition error: category tc93cat2 was not added to
            // LandmarkStore.");
            store.addCategory(tc93cat2);

            Landmark tc93lm = new Landmark(tc93lmstring, null, null, null);
            store.addLandmark(tc93lm, null); // null because the landmark
            // will be added to the category
            // via iVTH native interface.
            // assert(iVTH.countLandmarksByName(tc93lmstring) == 1, "TP19-ERR
            // TC93 pre-condition error: Landmark was not added to
            // LandmarkStore.");

            // int err = iVTH.addLandmarksToCategory(tc93lmstring, tc93cat1);
            // assert(err == 0, "TP19-ERR TC93 pre-condition error: (1) Landmark
            // was not sucessfully added to category.");
            // assert(iVTH.isLandmarkInCategory(tc93lmstring, tc93cat1),
            // "TP19-ERR TC93 pre-condition error: (2) Landmark was not
            // sucessfully added to category.");
            store.addLandmark(tc93lm, tc93cat1);

            // err = iVTH.addLandmarksToCategory(tc93lmstring, tc93cat2);
            // assert(err == 0, "TP19-ERR TC93 pre-condition error: (3) Landmark
            // was not sucessfully added to category.");
            // assert(iVTH.isLandmarkInCategory(tc93lmstring, tc93cat1),
            // "TP19-ERR TC93 pre-condition error: (4) Landmark was not
            // sucessfully added to category.");
            store.addLandmark(tc93lm, tc93cat2);

            // Flow:
            // ---------------------------------------------------------------
            store.deleteCategory(tc93cat2);
            // assert(!iVTH.categoryExists(tc93cat2), "TP19-ERR TC93 category
            // tc93cat2 was not deleted.");
            // assert(iVTH.categoryExists(tc93cat1), "TP19-ERR TC93 both
            // categories were deleted from LandmarkStore.");
            // assert(iVTH.countLandmarksByName(tc93lmstring) == 1, "TP19-ERR
            // TC93 Landmark was DELETED when deleting category!");
            // assert(iVTH.isLandmarkInCategory(tc93lmstring, tc93cat1),
            // "TP19-ERR TC93 Some strange error occurred!");
            assertTrue("", true);
        }
        catch (Throwable e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test LandmarkStore0412 in TCK (?)
    public void landmarkStore0412()
    {
        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            removeExistingStores();

            LandmarkStore.createLandmarkStore("store1");
            LandmarkStore.createLandmarkStore("store2");

            LandmarkStore store1 = LandmarkStore.getInstance("store1");
            LandmarkStore store2 = LandmarkStore.getInstance("store2");

            store1.addCategory("TEST_CATEGORY_1");
            store1.addCategory("TEST_CATEGORY_2");
            store2.addCategory("TEST_CATEGORY_1");

            AddressInfo addressInfo = new AddressInfo();
            for (int i = 1; i < 17; ++i)
            {
                addressInfo.setField(i, "Some text representing address stuff");
            }

            QualifiedCoordinates q1 = new QualifiedCoordinates(-30.0d, -10.0d,
                    Float.NaN, Float.NaN, Float.NaN);

            Landmark landmarkToAdd1 = new Landmark("landmark_1_name",
                                                   "landmark_1_description", q1, addressInfo);

            QualifiedCoordinates q2 = new QualifiedCoordinates(30.0d, 165.0d,
                    Float.NaN, Float.NaN, Float.NaN);

            Landmark landmarkToAdd2 = new Landmark("landmark_2_name",
                                                   "landmark_2_description", q2, addressInfo);

            QualifiedCoordinates q3 = new QualifiedCoordinates(30.0d, -165.0d,
                    Float.NaN, Float.NaN, Float.NaN);

            Landmark landmarkToAdd3 = new Landmark("landmark_3_name",
                                                   "landmark_3_description", q3, addressInfo);

            addLandmarkToStore(store1, landmarkToAdd1, "TEST_CATEGORY_1");
            addLandmarkToStore(store1, landmarkToAdd2, "TEST_CATEGORY_2");
            addLandmarkToStore(store2, landmarkToAdd3, "TEST_CATEGORY_1");

            // get Landmarks from store1
            Enumeration e = store1.getLandmarks(null, 10.0d, 50.0d, 150.0d,
                                                -150.0d);
            assertContinue("No landmarks found", e != null);
            Landmark received = (Landmark) e.nextElement();
            assertContinue("To many landmarks found", !e.hasMoreElements());

            String lmName = received.getName();
            assertContinue("Wrong landmark found in store", lmName
                           .equals(landmarkToAdd2.getName()));
            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test LandmarkStore0608 in TCK (?)
    public void landmarkStore0608()
    {
        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            removeExistingStores();

            LandmarkStore.createLandmarkStore(STORE_1_NAME);
            LandmarkStore.createLandmarkStore(STORE_2_NAME);

            LandmarkStore store1 = LandmarkStore.getInstance(STORE_1_NAME);
            LandmarkStore store2 = LandmarkStore.getInstance(STORE_2_NAME);

            Landmark LEGAL_LANDMARK_1 = new Landmark("landmark_1",
                    "description_text", VALID_QUALIFIED_COORDINATES, null);

            // adds landmark to the stores:
            addLandmarkToStore(store2, LEGAL_LANDMARK_1, null);

            addLandmarkToStore(store1, LEGAL_LANDMARK_1, null);

            // deletes the Landmark landmarkFromStore1 from the store:
            store1.deleteLandmark(LEGAL_LANDMARK_1);

            // Queries the Landmarks from the store and checks that the
            // returned Enumeration isn't null:
            Vector landmarks = getAllLandmarksFromStore(store2);

            // there should be two Landmarks in the Vector:
            assertContinue(
                "One Landmark was added to the newly created LandmarkStore. "
                + "deleteLandmark was called to another LandmarkStore with"
                + " same Landmark instance as parameter."
                + "The store containing the Landmark was expected to "
                + "remain as is. "
                + "Still, LandmarkStore.getLandmarks() returned "
                + "an Enumeration with " + landmarks.size()
                + " Landmarks.", landmarks.size() == 1);

            // Check if landmarks are equal not implemented

            // if (!LandmarkStoreHelpers
            // .areLandmarksEqual(
            // (Landmark) landmarks.firstElement(),
            // LEGAL_LANDMARK_1,
            // true)) {
            //
            // return m_helper.failTestCase( testCaseID,
            // "One Landmark was added to newly created LandmarkStore, "
            // + "but getLandmarks returned a Landmark instance not identical"
            // + "to added one.");
            // }

            landmarks = getAllLandmarksFromStore(store1);
            assertContinue(
                "One Landmark was stored to named "
                + "LandmarkStore and deleted, but the store still contains "
                + "Landmarks.", landmarks.size() == 0);
            removeExistingStores();
            assertTrue("", true);

        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test landmarkStore0701 in TCK (?)
    public void landmarkStore0701()
    {

        try
        {
            removeExistingStores();

            LandmarkStore store = LandmarkStore.getInstance(null);
            assertContinue(" Getting default LandmarkStore with getInstance "
                           + "returned null, but non-null value was expected.",
                           store != null);
            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test landmarkStore0702 in TCK (?)
    public void landmarkStore0702()
    {

        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            removeExistingStores();

            Landmark landmark = new Landmark("Landmark1", "Short description",
                                             VALID_QUALIFIED_COORDINATES,
                                             getAddressInfo_allFieldsLessThan30Chars());

            LandmarkStore def = LandmarkStore.getInstance(null);

            def.addLandmark(landmark, null);

            LandmarkStore.createLandmarkStore(STORE_1_NAME);
            LandmarkStore store = LandmarkStore.getInstance(STORE_1_NAME);
            assertContinue(" Getting LandmarkStore with getInstance "
                           + "returned null, but non-null value was expected.",
                           store != null);

            Vector landmarks = getAllLandmarksFromStore(store);
            assertContinue("Created new LandmarkStore instance and called."
                           + " getLandmarks() was expected to return zero landmarks, "
                           + " but enumeration containing " + landmarks.size()
                           + " landmarks was returned.", landmarks.size() == 0);
            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test landmarkStore0703 in TCK (?)
    public void landmarkStore0703()
    {

        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            removeExistingStores();

            LandmarkStore.createLandmarkStore(STORE_1_NAME);
            LandmarkStore store = LandmarkStore.getInstance(STORE_2_NAME);
            assertContinue(" Getting LandmarkStore with getInstance "
                           + "returned non-null when null value was expected.",
                           store == null);
            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test landmarkStore0801 in TCK (?)
    public void landmarkStore0801()
    {
        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            removeExistingStores();

            LandmarkStore.createLandmarkStore(STORE_1_NAME);
            LandmarkStore store = LandmarkStore.getInstance(STORE_1_NAME);
            assertContinue("Getting created LandmarkStore with getInstance "
                           + "returned null, when non-null value was expected.",
                           store != null);

            // get Landmarks from store
            Enumeration e = store.getLandmarks();
            assertContinue("Created new LandmarkStore instance and called"
                           + " getLandmarks(). The expected result was "
                           + "that zero Landmarks are returned. ", e == null);
            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test landmarkStore0802 in TCK (?)
    public void landmarkStore0802()
    {

        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            removeExistingStores();
            LandmarkStore.createLandmarkStore(STORE_1_NAME);
            LandmarkStore.createLandmarkStore(STORE_2_NAME);
            LandmarkStore store = LandmarkStore.getInstance(STORE_2_NAME);
            assertContinue(" Getting created LandmarkStore with getInstance "
                           + "returned null, but non-null value was expected.",
                           store != null);
            // get Landmarks from store
            Enumeration e = store.getLandmarks();
            assertContinue("Created new LandmarkStore instance and called"
                           + " getLandmarks(). The expected result was "
                           + "that zero Landmarks are returned. ", e == null);
            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test landmarkStore0803 in TCK (?)
    public void landmarkStore0803()
    {
        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            try
            {
                removeExistingStores();
                LandmarkStore.createLandmarkStore(STORE_1_NAME);
                LandmarkStore.createLandmarkStore(STORE_1_NAME);
            }
            catch (IllegalArgumentException ia)
            {
                return;
            }
            assertContinue(
                "Created new LandmarkStore with name already in use, "
                + "expected IllegalArgumentException to be thrown but "
                + "no exception was caught.", false);
            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test landmarkStore1201 in TCK (?)
    public void landmarkStore1201()
    {
        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            deleteAllLandmarksAndCategories();

            LandmarkStore store = LandmarkStore.getInstance(null);
            store.addCategory(LONG_CATEGORY_NAME);

            // check that it was actually added
            Enumeration categories = store.getCategories();
            assertContinue("store.getCategories() returned null!",
                           categories != null);

            while (categories.hasMoreElements())
            {
                String name = (String) categories.nextElement();
                assertContinue("Category name was null!", name != null);
                if (LONG_CATEGORY_NAME.equals(name))
                {
                    return;
                }
            }
            assertContinue("Added category with name \"" + LONG_CATEGORY_NAME
                           + "\" to default LandmarkStore but Category was not"
                           + "returned by getCategories method.", false);
            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test landmarkStore1202 in TCK (?)
    public void landmarkStore1202()
    {
        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            removeExistingStores();

            LandmarkStore.createLandmarkStore(STORE_1_NAME);
            LandmarkStore store1 = LandmarkStore.getInstance(STORE_1_NAME);

            LandmarkStore.createLandmarkStore(STORE_2_NAME);
            LandmarkStore store2 = LandmarkStore.getInstance(STORE_2_NAME);

            store1.addCategory(LONG_CATEGORY_NAME);

            // check that it was actually added to store1 only
            Enumeration categories = store2.getCategories();

            while (categories.hasMoreElements())
            {
                String name = (String) categories.nextElement();
                assertContinue(
                    "Two new LandmarkStores were created and new category "
                    + "was added to " + STORE_1_NAME
                    + ". Still same category was returned from \""
                    + STORE_2_NAME + "\".", !LONG_CATEGORY_NAME
                    .equals(name));
            }

            categories = store1.getCategories();

            while (categories.hasMoreElements())
            {
                String name = (String) categories.nextElement();
                if (LONG_CATEGORY_NAME.equals(name))
                {
                    return;
                }
            }
            assertContinue("Added category with name \"" + SHORT_CATEGORY_NAME
                           + "\" to default LandmarkStore but Category was not"
                           + "returned by getCategories mehtod.", false);
            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test landmarkStore1303 in TCK (?)
    public void landmarkStore1303()
    {
        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            removeExistingStores();

            LandmarkStore.createLandmarkStore(STORE_1_NAME);
            LandmarkStore store1 = LandmarkStore.getInstance(STORE_1_NAME);

            LandmarkStore.createLandmarkStore(STORE_2_NAME);
            LandmarkStore store2 = LandmarkStore.getInstance(STORE_2_NAME);

            String[] categories1 = getSupportedCategories(store1, 1);
            String[] categories2 = getSupportedCategories(store2, 1);

            assertContinue("Required number of supported categories are"
                           + "not available.", categories1 != null
                           && categories2 != null);

            // Check that categories were actually added
            Vector returned = getCategoriesAsVector(store2);
            assertContinue("One category was added to LandmarkStore"
                           + STORE_2_NAME
                           + " but it was not returned by method getCategories.",
                           returned.contains(categories2[0]));

            returned = getCategoriesAsVector(store1);

            assertContinue("One category was added to default LandmarkStore"
                           + STORE_1_NAME
                           + " but it was not returned by method getCategories.",
                           returned.contains(categories1[0]));

            store1.deleteCategory(categories1[0]);

            returned = getCategoriesAsVector(store1);
            assertContinue("One category was added to LandmarkStore"
                           + STORE_1_NAME
                           + " and after that it was deleted. Still it was "
                           + "returned by method getCategories.", !returned
                           .contains(categories1[0]));

            returned = getCategoriesAsVector(store2);
            assertContinue("One category was added to LandmarkStore"
                           + STORE_2_NAME
                           + " it was not deleted, but still it was not returned "
                           + " by method getCategories.", returned
                           .contains(categories2[0]));
            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    // Equivalent to test LandmarkStore1701 in TCK (?)
    public void landmarkStore1701()
    {
        try
        {
            System.gc();
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
            removeExistingStores();

            LandmarkStore.createLandmarkStore(LANDMARKSTORE_NAME);

            // gets the LandmarkStore:
            LandmarkStore landmarkStore = LandmarkStore
                                          .getInstance(LANDMARKSTORE_NAME);

            // gets the Landmark that is put to the LandmarkStore:
            Landmark landmark = new Landmark("sequential_landmark_name", null,
                                             new QualifiedCoordinates(80d, 45d, 250f, 20f, 30f), null);

            landmarkStore.addCategory(SEQUENTIAL_CATEGORY);

            // adds one Landmark to the LandmarkStore:
            addLandmarkToStore(landmarkStore, landmark, null);

            removeExistingStores();
            assertTrue("", true);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(), false);
        }
    }

    //------------------------ Helper methods -----------------------

    private String[] getSupportedCategories(LandmarkStore store, int count)
    throws Exception
    {

        assertContinue("internal error in " + "getSupportedCategories: "
                       + "LandmarkStore=" + store.toString() + " and count=" + count,
                       store != null && count > 0);

        String[] supported = new String[count];

        for (int i = 0; i < count; i++)
        {
            String categoryName = "Test_Category_" + i;

            store.addCategory(categoryName);
            supported[i] = categoryName;

        }

        // makes sure that the right number of categories or null
        // is always returned:
        assertContinue("Internal error: "
                       + "getSupportedCategories() method returned "
                       + "wrong number of categories. " + count
                       + " categories were requested, but " + supported.length
                       + " categories were returned.", supported == null
                       || supported.length == count);
        return supported;
    }

    private Vector getCategoriesAsVector(LandmarkStore store)
    {
        Vector v = new Vector();
        Enumeration e = store.getCategories();
        while (e.hasMoreElements())
        {
            v.addElement(e.nextElement());
        }
        return v;
    }

    private Vector getAllLandmarksFromStore(LandmarkStore landmarkStore)
    throws Exception
    {
        Enumeration landmarks = null;
        landmarks = landmarkStore.getLandmarks();

        assertContinue(
            "LandmarkStore.getLandmarks() returned an empty Enumeration. "
            + "If there are no Landmarks "
            + "in the LandmarkStore, it should return null.",
            landmarks == null || landmarks.hasMoreElements());

        // puts the Landmarks to a Vector:
        Vector result = new Vector();
        while (landmarks != null && landmarks.hasMoreElements())
        {
            Landmark current = (Landmark) landmarks.nextElement();
            assertContinue("LandmarkStore.getLandmarks() returned an "
                           + "Enumeration that had a null Landmark in it.",
                           current != null);
            result.addElement(current);
        }
        return result;
    }

    private static AddressInfo getAddressInfo_allFieldsLessThan30Chars()
    {

        // creates the AddressInfo object used in the tests
        // and sets its fields:
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setField(AddressInfo.BUILDING_FLOOR,
                             "value_building_floor 12");
        addressInfo.setField(AddressInfo.BUILDING_NAME, "value_building_name ");
        addressInfo.setField(AddressInfo.BUILDING_ROOM, "value_building_room");
        addressInfo.setField(AddressInfo.BUILDING_ZONE,
                             "value_building_zone east");
        addressInfo.setField(AddressInfo.CITY, "value_city Chicago");
        addressInfo.setField(AddressInfo.COUNTRY, "value_country");
        addressInfo.setField(AddressInfo.COUNTRY_CODE,
                             "value_country_code * # ");
        addressInfo.setField(AddressInfo.COUNTY, "value_county Helsinki");
        addressInfo.setField(AddressInfo.CROSSING1, "value_crossing1");
        addressInfo.setField(AddressInfo.CROSSING2, "value_crossing2");
        addressInfo.setField(AddressInfo.DISTRICT, "value_district");
        addressInfo.setField(AddressInfo.EXTENSION, "value_extension");
        addressInfo.setField(AddressInfo.PHONE_NUMBER,
                             "value_phoneNumber +358 1234");
        addressInfo
        .setField(AddressInfo.POSTAL_CODE, "value_postal_code 12345");
        addressInfo.setField(AddressInfo.STATE, "value_state Florida");
        addressInfo.setField(AddressInfo.STREET, "value_street Wall Street");
        addressInfo.setField(AddressInfo.URL, "value_url @ //www.com/?id=8#d");

        return addressInfo;
    }

    protected void addLandmarkToStore(LandmarkStore ls, Landmark landmark,
                                      String category) throws Exception
    {

        Enumeration e = ls.getLandmarks();
        int numLandmarksBefore = 0;
        if (e != null)
        {
            while (e.hasMoreElements())
            {
                Object o = e.nextElement();
                ++numLandmarksBefore;
            }
        }

        ls.addLandmark(landmark, category);

        // check that landmark was added
        e = ls.getLandmarks();
        assertContinue("Landmarks enumeration is null", e != null);

        int numLandmarksAfter = 0;
        while (e.hasMoreElements())
        {
            ++numLandmarksAfter;
            Object o = e.nextElement();
        }

        assertContinue("Expected only one landmark to be added",
                       numLandmarksAfter - numLandmarksBefore == 1);
    }

    protected void removeExistingStores() throws Exception
    {

        String[] stores = LandmarkStore.listLandmarkStores();

        if (stores != null)
        {
            for (int i = 0; i < stores.length; ++i)
            {
                LandmarkStore.deleteLandmarkStore(stores[i]);
            }
        }
    }

    protected void deleteAllLandmarksAndCategories() throws Exception
    {
        // Delete all the categories and Landmarks from the store
        LandmarkStore ls = LandmarkStore.getInstance(null);
        Enumeration c = ls.getCategories();
        while (c.hasMoreElements())
        {
            ls.deleteCategory((String) c.nextElement());
        }

        Enumeration l = ls.getLandmarks();
        if (l != null)
        {
            while (l.hasMoreElements())
            {
                ls.deleteLandmark((Landmark) l.nextElement());
            }
        }
    }
}