package io.tidepool.api;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.ServerError;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmList;
import io.tidepool.api.data.DeviceDataCBG;
import io.tidepool.api.data.Note;
import io.tidepool.api.data.Profile;
import io.tidepool.api.data.SharedUserId;
import io.tidepool.api.data.User;
import io.tidepool.api.util.MiscUtils;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class APIClientTest {
    @Before
    public void setUp() throws Exception {
        _setUpAPIClient(APIClient.DEVELOPMENT);
        mAPIClient.setRealmName("test.realm");
        mAPIClient.deleteRealm();
    }

    @After
    public void tearDown() {
        mAPIClient.deleteRealm();
    }
/*
    @Test
    public void testAllOnStaging() throws AssertionError {
        Log.v(this.getClass().getName(), "testAllOnStaging");
        _testAllOn(APIClient.STAGING);
    }

    @Test
    public void testAllOnProduction() throws AssertionError {
        Log.v(this.getClass().getName(), "testAllOnProduction");
        _testAllOn(APIClient.PRODUCTION);
    }
*/
    @Test
    public void testSignInSuccess() throws AssertionError {
        mAwaitDone = new AtomicBoolean(false);
        mAPIClient.signIn("ethan+urchintests@tidepool.org", "urchintests", new APIClient.SignInListener() {
            @Override
            public void signInComplete(User user, Exception exception) {
                mAWaitHashMap = new HashMap<String, Object>();
                mAWaitHashMap.put("user", user);
                mAWaitHashMap.put("exception", exception);
                mAwaitDone.set(true);
            }
        });
        await().atMost(10, TimeUnit.SECONDS).untilTrue(mAwaitDone);

        String sessionId = mAPIClient.getSessionId();
        User user = (User) mAWaitHashMap.get("user");
        Exception exception = (Exception) mAWaitHashMap.get("exception");
        assertThat(sessionId, notNullValue());
        assertThat(user, notNullValue());
        assertThat(exception, nullValue());
    }

    @Test
    public void testSignInFailure() throws AssertionError {
        mAwaitDone = new AtomicBoolean(false);
        mAPIClient.signIn("ethan+urchintests@tidepool.org", "wrong-password", new APIClient.SignInListener() {
            @Override
            public void signInComplete(User user, Exception error) {
                mAWaitHashMap = new HashMap<String, Object>();
                mAWaitHashMap.put("user", user);
                mAWaitHashMap.put("error", error);
                mAwaitDone.set(true);
            }
        });
        await().atMost(10, TimeUnit.SECONDS).untilTrue(mAwaitDone);

        User user = (User) mAWaitHashMap.get("user");
        Exception error = (Exception) mAWaitHashMap.get("error");
        assertThat(user, nullValue());
        assertThat(error, instanceOf(AuthFailureError.class));
    }

    @Test
    public void testSignOut() throws AssertionError {
        testSignInSuccess();

        mAwaitDone = new AtomicBoolean(false);
        mAPIClient.signOut(new APIClient.SignOutListener() {
            @Override
            public void signedOut(int responseCode, Exception error) {
                mAWaitHashMap = new HashMap<String, Object>();
                mAWaitHashMap.put("responseCode", responseCode);
                mAWaitHashMap.put("error", error);
                mAwaitDone.set(true);
            }
        });
        await().atMost(10, TimeUnit.SECONDS).untilTrue(mAwaitDone);

        int responseCode = (int) mAWaitHashMap.get("responseCode");
        Exception error = (Exception) mAWaitHashMap.get("error");
        assertThat(responseCode, is(200));
        assertThat(error, nullValue());
    }

    @Test
    public void testRefreshToken() throws AssertionError {
        testSignInSuccess();

        mAwaitDone = new AtomicBoolean(false);
        mAPIClient.refreshToken(new APIClient.RefreshTokenListener() {
            @Override
            public void tokenRefreshed(Exception error) {
                mAWaitHashMap = new HashMap<String, Object>();
                mAWaitHashMap.put("error", error);
                mAwaitDone.set(true);
            }
        });
        await().atMost(10, TimeUnit.SECONDS).untilTrue(mAwaitDone);

        String sessionId = mAPIClient.getSessionId();
        Exception error = (Exception) mAWaitHashMap.get("error");
        assertThat(sessionId, notNullValue());
        assertThat(error, nullValue());
    }

    @Test
    public void testGetProfile() {
        testSignInSuccess();

        Realm realm = mAPIClient.getRealmInstance();
        try {
            mAwaitDone = new AtomicBoolean(false);
            mAPIClient.getProfileForUserId(mAPIClient.getUser().getUserid(), new APIClient.ProfileListener() {
                @Override
                public void profileReceived(Profile profile, Exception error) {
                    mAWaitHashMap = new HashMap<String, Object>();
                    mAWaitHashMap.put("profile", profile);
                    mAWaitHashMap.put("error", error);
                    mAwaitDone.set(true);
                }
            });
            await().atMost(10, TimeUnit.SECONDS).untilTrue(mAwaitDone);

            Profile profile = (Profile) mAWaitHashMap.get("profile");
            Exception error = (Exception) mAWaitHashMap.get("error");
            assertThat(profile, notNullValue());
            assertThat(error, nullValue());
        } finally {
            realm.close();
        }
    }

    @Test
    public void testGetViewableUsers() {
        testSignInSuccess();

        Realm realm = mAPIClient.getRealmInstance();
        try {
            mAwaitDone = new AtomicBoolean(false);
            mAPIClient.getViewableUserIds(new APIClient.ViewableUserIdsListener() {
                @Override
                public void fetchComplete(RealmList<SharedUserId> userIds, Exception error) {
                    mAWaitHashMap = new HashMap<String, Object>();
                    mAWaitHashMap.put("userIds", userIds);
                    mAWaitHashMap.put("error", error);
                    mAwaitDone.set(true);
                }
            });
            await().atMost(10, TimeUnit.SECONDS).untilTrue(mAwaitDone);

            RealmList<SharedUserId> userIds = (RealmList<SharedUserId>) mAWaitHashMap.get("userIds");
            Exception error = (Exception) mAWaitHashMap.get("error");
            assertThat(userIds, notNullValue());
            assertThat(error, nullValue());
        } finally {
            realm.close();
        }
    }

    @Test
    public void testNoNotesBeforeEarlyTimePeriod() {
        testSignInSuccess();

        Realm realm = mAPIClient.getRealmInstance();
        try {
            mAwaitDone = new AtomicBoolean(false);

            Calendar from = Calendar.getInstance();
            from.set(Calendar.YEAR, 1950);
            from.set(Calendar.MONTH, Calendar.JANUARY);
            from.set(Calendar.DATE, 1);
            Calendar to = (Calendar) from.clone();
            to.set(Calendar.MONTH, Calendar.APRIL);
            mAPIClient.getNotes(mAPIClient.getUser().getUserid(), from.getTime(), to.getTime(), new APIClient.NotesListener() {
                @Override
                public void notesReceived(RealmList<Note> notes, Exception error) {
                    mAWaitHashMap = new HashMap<String, Object>();
                    mAWaitHashMap.put("notes", notes);
                    mAWaitHashMap.put("error", error);
                    mAwaitDone.set(true);
                }
            });
            await().atMost(10, TimeUnit.SECONDS).untilTrue(mAwaitDone);

            RealmList<Note> notes = (RealmList<Note>) mAWaitHashMap.get("notes");
            Exception error = (Exception) mAWaitHashMap.get("error");
            assertThat(notes, nullValue());
            assertThat(error, notNullValue());
            assertThat(error, instanceOf(ServerError.class));
            //assertThat(((ServerError)error).networkResponse.statusCode, is(404));
        } finally {
            realm.close();
        }
    }

    @Test
    public void testGetNotes() {
        testPostNote();

        Realm realm = mAPIClient.getRealmInstance();
        try {
            mAwaitDone = new AtomicBoolean(false);

            final Date to = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(to);
            c.add(Calendar.DATE, -1);
            final Date from = c.getTime();
            mAPIClient.getNotes(mAPIClient.getUser().getUserid(), from, to, new APIClient.NotesListener() {
                @Override
                public void notesReceived(RealmList<Note> notes, Exception error) {
                    mAWaitHashMap = new HashMap<String, Object>();
                    mAWaitHashMap.put("notes", notes);
                    mAWaitHashMap.put("error", error);
                    mAwaitDone.set(true);
                }
            });
            await().atMost(10, TimeUnit.SECONDS).untilTrue(mAwaitDone);

            RealmList<Note> notes = (RealmList<Note>) mAWaitHashMap.get("notes");
            Exception error = (Exception) mAWaitHashMap.get("error");
            assertThat(notes, notNullValue());
            assertThat(notes.size(), greaterThan(0));
            assertThat(error, nullValue());
        } finally {
            realm.close();
        }
    }

    @Test
    public void testPostNote() {
        testSignInSuccess();

        Realm realm = mAPIClient.getRealmInstance();
        try {
            Note note = new Note();
            note.setMessagetext("New note added from test.");
            note.setTimestamp(new Date());
            note.setGroupid(mAPIClient.getUser().getUserid());
            note.setUserid(mAPIClient.getUser().getUserid());
            note.setAuthorFullName(MiscUtils.getPrintableNameForUser(mAPIClient.getUser()));
            note.setGuid(UUID.randomUUID().toString());

            mAwaitDone = new AtomicBoolean(false);
            mAPIClient.postNote(note, new APIClient.PostNoteListener() {
                @Override
                public void notePosted(Note note, Exception error) {
                    mAWaitHashMap = new HashMap<String, Object>();
                    mAWaitHashMap.put("error", error);
                    mAwaitDone.set(true);
                }
            });
            await().atMost(10, TimeUnit.SECONDS).untilTrue(mAwaitDone);

            Exception error = (Exception) mAWaitHashMap.get("error");
            assertThat(error, nullValue());
        } finally {
            realm.close();
        }
    }

    @Test
    public void testUploadDeviceData() {
        testSignInSuccess();

        Realm realm = mAPIClient.getRealmInstance();
        try {
            List<DeviceDataCBG> data = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                DeviceDataCBG datum = new DeviceDataCBG();
                datum.setDeviceId("test_cgm_device_id");
                datum.setUploadId("");

                datum.setUnits("mg/dL");
                datum.setValue(100 + 5 * i);

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.MINUTE, -5 * i);
                datum.setTime(cal.getTime());

                data.add(datum);
            }

            mAwaitDone = new AtomicBoolean(false);
            mAPIClient.uploadDeviceData(data, new APIClient.UploadDeviceDataListener() {
                @Override
                public void dataUploaded(List data, Exception error) {
                    mAWaitHashMap = new HashMap<String, Object>();
                    mAWaitHashMap.put("error", error);
                    mAwaitDone.set(true);
                }
            });
            await().atMost(10, TimeUnit.SECONDS).untilTrue(mAwaitDone);

            Exception error = (Exception) mAWaitHashMap.get("error");
            assertThat(error, nullValue());
        } finally {
            realm.close();
        }
    }

    private void _testAllOn(String environment) throws AssertionError {
        _setUpAPIClient(environment);

        Pattern allOnPattern = Pattern.compile("^.*AllOn.*$");
        for (Method method : getClass().getDeclaredMethods()) {
            String methodName = method.getName();
            if (method.isAnnotationPresent(Test.class) &&
                    !allOnPattern.matcher(methodName).matches()) {
                Log.v(this.getClass().getName(), "invoking method: " + methodName);
                try {
                    method.invoke(this);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                    if (e.getTargetException() instanceof AssertionError) {
                        throw (AssertionError) e.getTargetException();
                    }
                }
            }
        }
    }

    private void _setUpAPIClient(String environment) {
        mAPIClient = new APIClient(InstrumentationRegistry.getInstrumentation().getTargetContext(), environment);
        Log.v(this.getClass().getName(), "APIClient set up for environment: " + environment);
    }

    private APIClient mAPIClient;
    private AtomicBoolean mAwaitDone;
    private HashMap<String, Object> mAWaitHashMap;
}
