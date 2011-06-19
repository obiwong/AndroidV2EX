package com.htbest2000.v2ex.test;

import java.util.HashMap;

import com.htbest2000.reflect.New;

import junit.framework.TestCase;

public class TestReflect extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public static class Foobar {
		public String foo;
		public String bar;
		public long id;
		public int version;
		public Foobar(){}
	}
	
	public void testNewInflate() {
		New<Foobar> foobarNew = new New<Foobar>();
	    HashMap<String, Object> maps = new HashMap<String, Object>();

        maps.put("foo", "the foo");
        maps.put("bar", "the bar");
        maps.put("bar", "the bar");
        maps.put("id", new Long(1));
        maps.put("version", new Integer(3));

        Foobar foobar = foobarNew.inflate(maps, Foobar.class);
        assertNotNull(foobar);
        assertEquals( "the foo", foobar.foo );
        assertEquals( "the bar", foobar.bar );
        assertEquals( 1, foobar.id );
        assertEquals( 3, foobar.version );
	}

}
