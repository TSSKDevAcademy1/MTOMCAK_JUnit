package microunit;

public class Assert {
	
	public static void assertEquals(Object current, Object expected) {
		if ((current == null &&  expected != null)
				|| (current != null &&  expected == null) 
				|| (!current.equals(expected) && expected != null)) {
			throw new AssertionException(String.format("Expected value was %s but current is %s", expected,current));
		}
	}

}
