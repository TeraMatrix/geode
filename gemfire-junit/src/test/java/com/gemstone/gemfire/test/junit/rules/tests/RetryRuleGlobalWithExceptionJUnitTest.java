package com.gemstone.gemfire.test.junit.rules.tests;

import static com.gemstone.gemfire.test.junit.rules.tests.RunTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.gemstone.gemfire.test.junit.Retry;
import com.gemstone.gemfire.test.junit.categories.UnitTest;
import com.gemstone.gemfire.test.junit.rules.RetryRule;

/**
 * Unit tests for Retry JUnit Rule involving global scope (ie Rule affects all 
 * tests in the test case) with failures due to an Exception.
 * 
 * @author Kirk Lund
 */
@Category(UnitTest.class)
public class RetryRuleGlobalWithExceptionJUnitTest {
  
  @Test
  public void zeroIsIllegal() {
    Result result = runTest(ZeroIsIllegal.class);
    
    assertThat(result.wasSuccessful()).isFalse();
    
    List<Failure> failures = result.getFailures();
    assertThat(failures.size()).as("Failures: " + failures).isEqualTo(1);

    Failure failure = failures.get(0);
    assertThat(failure.getException()).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage(ZeroIsIllegal.message);
    assertThat(ZeroIsIllegal.count).isEqualTo(0);
  }
  
  @Test
  public void failsWithOne() {
    Result result = runTest(FailsWithOne.class);
    
    assertThat(result.wasSuccessful()).isFalse();
    
    List<Failure> failures = result.getFailures();
    assertThat(failures.size()).as("Failures: " + failures).isEqualTo(1);

    Failure failure = failures.get(0);
    assertThat(failure.getException()).isExactlyInstanceOf(CustomException.class).hasMessage(FailsWithOne.message);
    assertThat(FailsWithOne.count).isEqualTo(1);
  }
  
  @Test
  public void passesWithOne() {
    Result result = runTest(PassesWithOne.class);
    
    assertThat(result.wasSuccessful()).isTrue();
  }
  
  @Test
  public void passesWithUnused() {
    Result result = runTest(PassesWhenUnused.class);
    
    assertThat(result.wasSuccessful()).isTrue();
  }
  
  @Test
  public void failsOnSecondAttempt() {
    Result result = runTest(FailsOnSecondAttempt.class);
    
    assertThat(result.wasSuccessful()).isFalse();
    
    List<Failure> failures = result.getFailures();
    assertThat(failures.size()).as("Failures: " + failures).isEqualTo(1);

    Failure failure = failures.get(0);
    assertThat(failure.getException()).isExactlyInstanceOf(CustomException.class).hasMessage(FailsOnSecondAttempt.message);
    assertThat(FailsOnSecondAttempt.count).isEqualTo(2);
  }

  @Test
  public void passesOnSecondAttempt() {
    Result result = runTest(PassesOnSecondAttempt.class);
    
    assertThat(result.wasSuccessful()).isTrue();
    assertThat(PassesOnSecondAttempt.count).isEqualTo(2);
  }
  
  @Test
  public void failsOnThirdAttempt() {
    Result result = runTest(FailsOnThirdAttempt.class);
    
    assertThat(result.wasSuccessful()).isFalse();
    
    List<Failure> failures = result.getFailures();
    assertThat(failures.size()).as("Failures: " + failures).isEqualTo(1);

    Failure failure = failures.get(0);
    assertThat(failure.getException()).isExactlyInstanceOf(CustomException.class).hasMessage(FailsOnThirdAttempt.message);
    assertThat(FailsOnThirdAttempt.count).isEqualTo(3);
  }

  @Test
  public void passesOnThirdAttempt() {
    Result result = runTest(PassesOnThirdAttempt.class);
    
    assertThat(result.wasSuccessful()).isTrue();
    assertThat(PassesOnThirdAttempt.count).isEqualTo(3);
  }
  
  public static class CustomException extends Exception {
    private static final long serialVersionUID = 1L;
    public CustomException(final String message) {
      super(message);
    }
  }
  
  public static class ZeroIsIllegal {
    protected static int count;
    protected static final String message = "Retry count must be greater than zero";

    @Rule
    public RetryRule retryRule = new RetryRule(0);

    @Test
    public void zeroIsIllegal() throws Exception {
      count++;
    }
  }
  
  public static class FailsWithOne {
    protected static int count;
    protected static String message;

    @Rule
    public RetryRule retryRule = new RetryRule(1);

    @Test
    public void failsWithOne() throws Exception {
      count++;
      message = "Failing " + count;
      throw new CustomException(message);
    }
  }
  
  public static class PassesWithOne {
    protected static int count;

    @Rule
    public RetryRule retryRule = new RetryRule(1);

    @Test
    public void passesWithOne() throws Exception {
      count++;
    }
  }
  
  public static class PassesWhenUnused {
    protected static int count;

    @Rule
    public RetryRule retryRule = new RetryRule(2);

    @Test
    public void passesWithUnused() throws Exception {
      count++;
    }
  }
  
  public static class FailsOnSecondAttempt {
    protected static int count;
    protected static String message;

    @Rule
    public RetryRule retryRule = new RetryRule(2);

    @Test
    @Retry(2)
    public void failsOnSecondAttempt() throws Exception {
      count++;
      message = "Failing " + count;
      throw new CustomException(message);
    }
  }
  
  public static class PassesOnSecondAttempt {
    protected static int count;
    protected static String message;
    
    @Rule
    public RetryRule retryRule = new RetryRule(2);

    @Test
    @Retry(2)
    public void failsOnSecondAttempt() throws Exception {
      count++;
      if (count < 2) {
        message = "Failing " + count;
        throw new CustomException(message);
      }
    }
  }
  
  public static class FailsOnThirdAttempt {
    protected static int count;
    protected static String message;

    @Rule
    public RetryRule retryRule = new RetryRule(3);

    @Test
    @Retry(3)
    public void failsOnThirdAttempt() throws Exception {
      count++;
      message = "Failing " + count;
      throw new CustomException(message);
    }
  }

  public static class PassesOnThirdAttempt {
    protected static int count;
    protected static String message;

    @Rule
    public RetryRule retryRule = new RetryRule(3);

    @Test
    public void failsOnThirdAttempt() throws Exception {
      count++;
      if (count < 3) {
        message = "Failing " + count;
        throw new CustomException(message);
      }
    }
  }
}