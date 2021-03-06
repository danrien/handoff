package com.namehillsoftware.handoff.promises.GivenAPromiseThatIsRejected.AndEventuallyContinuingWithResponseAndRejection;

import com.namehillsoftware.handoff.promises.Promise;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WhenAnotherReturningPromiseIsExpected {

	private Integer nextReturningPromiseResult;
	private boolean isCalled;

	private static final Exception thrownException = new Exception();
	private static Throwable caughtException;

	@Before
	public void before() {
		new Promise<>(thrownException)
				.eventually(result -> new Promise<>(330 + result.hashCode()), err -> {
					caughtException = err;
					return new Promise<>(5);
				})
				.then(nextResult -> nextReturningPromiseResult = nextResult)
				.excuse(err -> isCalled = true);
	}

	@Test
	public void thenTheRejectionIsCorrect() {
		assertThat(caughtException).isEqualTo(thrownException);
	}

	@Test
	public void thenTheNextActionIsCalled() {
		assertThat(nextReturningPromiseResult.intValue()).isEqualTo(5);
	}

	@Test
	public void thenTheErrorIsNotCalled() {
		assertThat(isCalled).isFalse();
	}
}
