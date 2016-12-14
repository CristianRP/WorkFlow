package com.gruporosul.workflow;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gruporosul.workflow.activity.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Cristian Ramírez on 2/12/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    private String mUserName;
    private String mPassword;
    private String mSuccessString;
    private String mUserNameWrong;
    private String mPasswordWrong;
    private String mFailedString;
    private String mEmptyFieldsString;

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initVariables() {
        mUserName = "sfajardo";
        mPassword = "sfajardo";
        mSuccessString = "Logeado con éxito!";
        mEmptyFieldsString = "Tienes que ingresar tus datos";
        mUserNameWrong = "test";
        mPasswordWrong = "test";
        mFailedString = "Ingresa los datos correctos";
    }

    @Test
    public void test1CheckLoginSuccess() {
        onView(withId(R.id.textUser)).perform(typeText(mUserName));
        closeSoftKeyboard();
        onView(withId(R.id.textPassword)).perform(typeText(mPassword));
        closeSoftKeyboard();
        onView(withId(R.id.fabLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(mSuccessString))).check(matches(isDisplayed()));
    }

    @Test
    public void test2CheckLoginFailed() {
        onView(withId(R.id.textUser)).perform(typeText(mUserNameWrong));
        closeSoftKeyboard();
        onView(withId(R.id.textPassword)).perform(typeText(mPasswordWrong));
        closeSoftKeyboard();
        onView(withId(R.id.fabLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(mFailedString))).check(matches(isDisplayed()));
    }

}
