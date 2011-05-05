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
				new Topics.Topic(1, "title 1", "url 1", "content 1", "content_rendered 1", 1,
						1, "member username 1",
						1, "node name 1", "node title 1", "node title_alternative 1", "node url 1", 1),
				new Topics.Topic(2, "title 2", "url 2", "content 2", "content_rendered 2", 2,
						2, "member username 2",
						2, "node name 2", "node title 2", "node title_alternative 2", "node url 2", 2),
				new Topics.Topic(3, "title 3", "url 3", "content 3", "content_rendered 3", 3,
						3, "member username 3",
						3, "node name 3", "node title 3", "node title_alternative 3", "node url 3", 3),
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
				
				assertEquals(expects[count].member_id, topic.member_id);
				assertEquals(expects[count].member_username, topic.member_username);
				
				assertEquals(expects[count].node_id, topic.node_id);
				assertEquals(expects[count].node_name, topic.node_name);
				assertEquals(expects[count].node_title, topic.node_title);
				assertEquals(expects[count].node_title_alternative, topic.node_title_alternative);
				assertEquals(expects[count].node_url, topic.node_url);
				assertEquals(expects[count].node_topics, topic.node_topics);
				
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
