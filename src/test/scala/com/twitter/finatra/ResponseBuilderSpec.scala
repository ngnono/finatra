/**
 * Copyright (C) 2012 Twitter Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twitter.finatra.test

import org.jboss.netty.util.CharsetUtil.UTF_8

import com.twitter.finatra.Response
import com.twitter.finatra.View

class MockView(val title:String) extends View {
  val template = "mock.mustache"
}

class ResponseSpec extends ShouldSpec {
  def resp = new Response
  def view = new MockView("howdy view")

  ".ok" should "return a 200 response" in {
    resp.ok.status should equal (200)
  }

  ".notFound" should "return a 404 response" in {
    resp.notFound.status should equal (404)
  }

  ".status(201)" should "return a 201 response" in {
    resp.status(201).status should equal (201)
  }

  ".plain()" should "return a 200 plain response" in {
    val response = resp.plain("howdy")

    response.status should equal (200)
    response.strBody.get should equal ("howdy")
    response.contentType should equal (Some("text/plain"))
  }

  ".nothing()" should "return a 200 empty response" in {
    val response = resp.nothing

    response.status should equal (200)
    response.strBody.get should equal ("")
    response.contentType should equal (Some("text/plain"))
  }

  ".html()" should "return a 200 html response" in {
    val response = resp.html("<h1>howdy</h1>")

    response.status should equal (200)
    response.strBody.get should equal ("<h1>howdy</h1>")
    response.contentType should equal (Some("text/html"))
  }

  ".json()" should "return a 200 json response" in {
    val response = resp.json(Map("foo" -> "bar"))
    val body     = response.build.getContent.toString(UTF_8)

    response.status should equal (200)
    body should equal ("""{"foo":"bar"}""")
    response.contentType should equal (Some("application/json"))
  }

  ".view()" should "return a 200 view response" in {
    val response = resp.view(view)
    val body     = response.build.getContent.toString(UTF_8)

    response.status should equal (200)
    body should include ("howdy view")
  }

  ".static()" should "return a 200 static file" in {
    val response = resp.static("dealwithit.gif")

    response.status should equal (200)
    response.contentType should equal (Some("image/gif"))
  }
}
