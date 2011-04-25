package com.htbest2000.v2ex.test;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.test.AndroidTestCase;

import com.htbest2000.v2ex.api.Nodes;
import com.htbest2000.v2ex.api.Site;
import com.htbest2000.v2ex.api.Topics;
import com.htbest2000.v2ex.api.Nodes.Node;
import com.htbest2000.v2ex.api.Topics.Topic;

public class TestApi extends AndroidTestCase{
	AssetManager mAssetMgr;
	
	@Override
	protected void setUp() throws Exception {
		mAssetMgr = mContext.getResources().getAssets();
		assertNotNull(mAssetMgr);
		super.setUp();
	}

	public void testSite() {
		InputStream in = null;
		try {
			in = mAssetMgr.open("siteinfo.json");
		} catch (IOException e) {
			e.printStackTrace();
			assertNotNull(null);
		}

		assertNotNull(in);

		try {
			Site site = Site.create(in);
			in.close();
			assertNotNull(site);
			assertEquals("the title", site.title);
			assertEquals("the slogan", site.slogan);
			assertEquals("the description", site.description);
			assertEquals("the domain", site.domain);
		} catch (IOException e) {
			e.printStackTrace();
			assertNotNull(null);
		}
	}
	
	public void testTopics() {
		final Topics.Topic expects[] = {
				new Topics.Topic(12345, "title 1", "url 1", "content 1", "rend 1", 1),
				new Topics.Topic(54321, "title 2", "url 2", "", "", 2),
				new Topics.Topic(1111, "title 3", "url 3", "content 3", "rend 3", 0),
		};
		Topics topics = new Topics();
		topics.setVisitor(new Topics.Visitor() {
			int count = 0;
			
			@Override
			public void visit(Topic topic) {
				assertNotNull(topic);
				assertEquals(expects[count].id, topic.id);
				assertEquals(expects[count].title, topic.title);
				assertEquals(expects[count].url, topic.url);
				assertEquals(expects[count].content, topic.content);
				assertEquals(expects[count].content_rendered, topic.content_rendered);
				assertEquals(expects[count].replies, topic.replies);
				count++;
			}
		});
		
		InputStream in = null;
		try {
			in = mAssetMgr.open("latest.json");
		} catch (IOException e) {
			e.printStackTrace();
			assertNotNull(null);
		}
		assertNotNull(in);
		
		try {
			topics.travel(in);
		} catch (IOException e) {
			e.printStackTrace();
			assertNotNull(null);
		}
	}
	
	public void testNodes() {
		final Nodes.Node[] expects = {
			new Nodes.Node(28, "picky", "http://www.v2ex.com/go/picky", "Project Picky",
					"Project Picky", 73, null, null, "2010-04-25 18:23:09.059402"),
			new Nodes.Node(61, "mysql", "http://www.v2ex.com/go/mysql", "MySQL",
					"MySQL", 13, null, null, "2010-04-30 22:38:38.126296")
		};
		
		Nodes nodes = new Nodes();
		nodes.setVisitor( new Nodes.Visitor() {
			int count = 0;

			@Override
			public void visit(Node node) {
				assertNotNull(node);
				assertEquals(expects[count].id, node.id);
				assertEquals(expects[count].name, node.name);
				assertEquals(expects[count].url, node.url);
				assertEquals(expects[count].title, node.title);
				assertEquals(expects[count].title_alternative, node.title_alternative);
				assertEquals(expects[count].topics, node.topics);
				assertEquals(expects[count].header, node.header);
				assertEquals(expects[count].footer, node.footer);
				assertEquals(expects[count].created, node.created);
				count++;
			}
		});
		
		InputStream in = null;
		try {
			in = mAssetMgr.open("nodes.json");
		} catch (IOException e) {
			e.printStackTrace();
			assertNotNull(null);
		}
		assertNotNull(in);
		
		try {
			nodes.travel(in);
		} catch (IOException e) {
			e.printStackTrace();
			assertNotNull(null);
		}
	}
}
