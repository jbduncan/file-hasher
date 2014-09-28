package uk.co.bluettduncanj.utils;

import java.io.Serializable;
import java.util.Objects;

public class Pair<A, B> implements Serializable {

	private final A first;
	private final B second;

	private Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public static <A, B> Pair<A, B> of(A first, B second) {
		return new Pair<>(first, second);
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ')';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Pair pair = (Pair) o;

		return Objects.equals(this.first, pair.first) &&
				Objects.equals(this.second, pair.second);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.first, this.second);
	}
}
