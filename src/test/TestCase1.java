package test;

import microunit.Test;

import static microunit.Assert.*;

import microunit.PostConstruct;
import microunit.PreDestroy;

public class TestCase1 {
	
	
	@Test
	public void method1() {
		assertEquals(5, null);	
	}

	@Test
	public void method2() {
		assertEquals(1, 1);
	}
	
	@PostConstruct
	public void methodPost() {
		System.out.println("methodPostConstruct");
	}
	
	@PreDestroy
	public void methodDestroy() {
		System.out.println("method3");
	}

	@Test
	public void method3() {
		System.out.println("method3");
	}
}
