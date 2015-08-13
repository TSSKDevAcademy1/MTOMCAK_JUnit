package microunit;

import java.awt.Window.Type;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestRunner {
	private static long timeTestCase, timeMethod;
	private int failedMethods = 0, methodsCount = 0;

	public void processTestSuite(String filename) {
		try (BufferedReader r = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = r.readLine()) != null) {
				// erase blankspace
				line = line.trim();
				if (!"".equals(line)) {
					runTests(line);
				}
			}
		} catch (Exception e) {
			System.err.println("Cannot read test suite file: " + filename);
			e.printStackTrace();
		}
	}

	public void runTests(String className) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (Exception e) {
			System.err.println("Not valid test class: " + className);
			return;
		}

		Object testCase;
		try {
			testCase = clazz.newInstance();
		} catch (Exception e) {
			System.err.println("Cannot create instance for test class: " + className);
			return;
		}
		
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(Test.class)) {
				
			}
		}
		//runMethodByAnnotation( testCase, Test.class); 	// run all PostConstruct annotations
		runMethod(clazz, testCase);			// run all Test annotations
	}

	private void runMethodByAnnotation(Object testCase, Class<? extends Annotation> clazz) {
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(clazz)) {
				timeMethod = System.currentTimeMillis();
				System.out.printf("Running PostConstruct: %s.%s\n", clazz.getName(), method.getName());
				methodsCount++;
				try {
					method.invoke(testCase);
					System.out.printf("Test successful: %s.%s\n", clazz.getName(), method.getName());
				} catch (InvocationTargetException e) {
					failedMethods++;
					System.out.printf("Test failed: %s.%s\n", clazz.getName(), method.getName());
					e.getCause().printStackTrace();
				} catch (Exception e) {
					System.out.printf("Not valid test method: %s.%s\n", clazz.getName(), method.getName());
				}
				System.out.println("Processing time: " + (System.currentTimeMillis() - timeMethod) + " ms\n");
			}
		}
		writeResult();
		counterReset();
	}

	private void writeResult() {
		//System.out.println("Processing time: " + (System.currentTimeMillis() - timeTestCase) + " ms");
		System.out.println("Successfull test: " + (methodsCount - failedMethods) + "/" + methodsCount + "\n");
	}

	private void counterReset() {
		failedMethods = 0;
		methodsCount = 0;
	}

	/**Run all test methods.*/
	private void runMethod(Class<?> clazz, Object testCase) {
		//Annotation clazzs = clazz.getAnnotation(Test.class);
//		Class<?> t = PostConstruct.class;
//		System.out.println(PostConstruct.class +" \ninterface " + clazz.getTypeName() + "\n\n\n");
		
		timeTestCase = System.currentTimeMillis();
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(Test.class)) {
				timeMethod = System.currentTimeMillis();
				System.out.printf("Running test: %s.%s\n", clazz.getName(), method.getName());
				methodsCount++;
				try {
					method.invoke(testCase);
					System.out.printf("Test successful: %s.%s\n", clazz.getName(), method.getName());
				} catch (InvocationTargetException e) {
					failedMethods++;
					System.out.printf("Test failed: %s.%s\n", clazz.getName(), method.getName());
					e.getCause().printStackTrace();
				} catch (Exception e) {
					System.out.printf("Not valid test method: %s.%s\n", clazz.getName(), method.getName());
				}
				System.out.println("Processing time: " + (System.currentTimeMillis() - timeMethod) + " ms\n");	
			}
		}
		writeResult();
		counterReset();
	}
		

	public static void main(String[] args) throws Exception {
		TestRunner runner = new TestRunner();
		runner.processTestSuite("tests");
	}
}
