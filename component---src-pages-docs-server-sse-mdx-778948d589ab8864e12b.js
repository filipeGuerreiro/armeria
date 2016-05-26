(window.webpackJsonp=window.webpackJsonp||[]).push([[45],{"/94A":function(e){e.exports=JSON.parse('{"root":["index","setup"],"References":{"Community articles":"/community/articles","API documentation":"https://javadoc.io/doc/com.linecorp.armeria/armeria-javadoc/latest/index.html","Release notes":"/release-notes"},"Server":["server-basics","server-decorator","server-grpc","server-thrift","server-docservice","server-annotated-service","server-http-file","server-servlet","server-access-log","server-cors","server-sse"],"Client":["client-http","client-thrift","client-grpc","client-decorator","client-retrofit","client-custom-http-headers","client-timeouts","client-retry","client-circuit-breaker","client-service-discovery"],"Advanced":["advanced-logging","advanced-structured-logging","advanced-custom-attributes","advanced-structured-logging-kafka","advanced-unit-testing","advanced-production-checklist","advanced-zipkin","advanced-zookeeper","advanced-saml","advanced-spring-webflux-integration","advanced-dropwizard-integration"]}')},AesF:function(e,t,r){"use strict";r.r(t),r.d(t,"pageTitle",(function(){return o})),r.d(t,"_frontmatter",(function(){return c})),r.d(t,"default",(function(){return m}));var n,a=r("8o2o"),s=(r("q1tI"),r("7ljp")),i=r("xCMr"),o="Serving Server-Sent Events",c={},v=(n="Tip",function(e){return console.warn("Component "+n+" was not imported, exported, or provided by MDXProvider as global scope"),Object(s.b)("div",e)}),l={pageTitle:o,_frontmatter:c},u=i.a;function m(e){var t=e.components,r=Object(a.a)(e,["components"]);return Object(s.b)(u,Object.assign({},l,r,{components:t,mdxType:"MDXLayout"}),Object(s.b)("h1",{id:"serving-server-sent-events",style:{position:"relative"}},Object(s.b)("a",Object.assign({parentName:"h1"},{href:"#serving-server-sent-events","aria-label":"serving server sent events permalink",className:"anchor before"}),Object(s.b)("svg",Object.assign({parentName:"a"},{"aria-hidden":"true",focusable:"false",height:"16",version:"1.1",viewBox:"0 0 16 16",width:"16"}),Object(s.b)("path",Object.assign({parentName:"svg"},{fillRule:"evenodd",d:"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"})))),"Serving Server-Sent Events"),Object(s.b)("h6",{className:"inlinePageToc",role:"navigation"},"Table of contents"),Object(s.b)("ul",null,Object(s.b)("li",{parentName:"ul"},Object(s.b)("a",Object.assign({parentName:"li"},{href:"#adjusting-the-request-timeout"}),"Adjusting the request timeout"))),Object(s.b)(v,{mdxType:"Tip"},Object(s.b)("p",null,"Visit ",Object(s.b)("a",Object.assign({parentName:"p"},{href:"https://github.com/line/armeria-examples"}),"armeria-examples")," to find a fully working example.")),Object(s.b)("p",null,"A traditional web page has to send a request to the server in order to receive new data.\nWith ",Object(s.b)("a",Object.assign({parentName:"p"},{href:"https://www.w3.org/TR/eventsource/"}),"Server-Sent Events"),", however, it is possible for a server to push a new data to\nthe web page whenever it wants to."),Object(s.b)("p",null,"Armeria provides several factory methods by ",Object(s.b)("a",Object.assign({parentName:"p"},{href:"type://ServerSentEvents:https://javadoc.io/doc/com.linecorp.armeria/armeria-javadoc/latest/com/linecorp/armeria/server/streaming/ServerSentEvents.html"}),"type://ServerSentEvents")," class which help a user to easily send\nan event stream to the client. The following example shows how to build a server which serves services\nsending a response with ",Object(s.b)("a",Object.assign({parentName:"p"},{href:"https://www.w3.org/TR/eventsource/"}),"Server-Sent Events"),"."),Object(s.b)("pre",null,Object(s.b)("code",Object.assign({parentName:"pre"},{className:"language-java"}),'import com.linecorp.armeria.common.sse.ServerSentEvent;\nimport com.linecorp.armeria.server.Server;\nimport com.linecorp.armeria.server.streaming.ServerSentEvents;\nimport reactor.core.publisher.Flux;\n\nServer server =\n        Server.builder()\n              // Emit Server-Sent Events with the SeverSentEvent instances published by a publisher.\n              .service("/sse1",\n                       (ctx, req) -> ServerSentEvents.fromPublisher(\n                               Flux.just(ServerSentEvent.ofData("foo"), ServerSentEvent.ofData("bar"))))\n              // Emit Server-Sent Events with converting instances published by a publisher into\n              // ServerSentEvent instances.\n              .service("/sse2",\n                       (ctx, req) -> ServerSentEvents.fromPublisher(\n                               Flux.just("foo", "bar"), ServerSentEvent::ofData))\n              .build();\n')),Object(s.b)("p",null,"Of course, Armeria provides ",Object(s.b)("a",Object.assign({parentName:"p"},{href:"type://@ProducesEventStream:https://javadoc.io/doc/com.linecorp.armeria/armeria-javadoc/latest/com/linecorp/armeria/server/annotation/ProducesEventStream.html"}),"type://@ProducesEventStream")," annotation in order to convert the result objects\nreturned from an annotated service method into ",Object(s.b)("a",Object.assign({parentName:"p"},{href:"https://www.w3.org/TR/eventsource/"}),"Server-Sent Events"),".\nThe following example shows how to use the annotation."),Object(s.b)("pre",null,Object(s.b)("code",Object.assign({parentName:"pre"},{className:"language-java"}),'import com.linecorp.armeria.common.sse.ServerSentEvent;\nimport com.linecorp.armeria.server.annotation.Get;\nimport com.linecorp.armeria.server.annotation.ProducesEventStream;\nimport org.reactivestreams.Publisher;\n\n@Get("/sse")\n@ProducesEventStream\npublic Publisher<ServerSentEvent> sse() {\n    return Flux.just(ServerSentEvent.ofData("foo"), ServerSentEvent.ofData("bar"));\n}\n')),Object(s.b)("h2",{id:"adjusting-the-request-timeout",style:{position:"relative"}},Object(s.b)("a",Object.assign({parentName:"h2"},{href:"#adjusting-the-request-timeout","aria-label":"adjusting the request timeout permalink",className:"anchor before"}),Object(s.b)("svg",Object.assign({parentName:"a"},{"aria-hidden":"true",focusable:"false",height:"16",version:"1.1",viewBox:"0 0 16 16",width:"16"}),Object(s.b)("path",Object.assign({parentName:"svg"},{fillRule:"evenodd",d:"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"})))),"Adjusting the request timeout"),Object(s.b)("p",null,"An event stream may be sent for a longer period than the configured timeout depending on the application.\nIt even can continue infinitely, for example streaming stock quotes. Such a long running stream may be\nterminated prematurely because Armeria has the default request timeout of ",Object(s.b)("inlineCode",{parentName:"p"},"10")," seconds, i.e. your stream\nwill be broken after 10 seconds at most. Therefore, you must adjust the timeout if your event stream lasts\nlonger than the configured timeout. The following example shows how to adjust the timeout of a single request.\nAs you might know, it is not only for ",Object(s.b)("a",Object.assign({parentName:"p"},{href:"https://www.w3.org/TR/eventsource/"}),"Server-Sent Events"),", so you can use this method for\nany requests which you want to adjust timeout for."),Object(s.b)("pre",null,Object(s.b)("code",Object.assign({parentName:"pre"},{className:"language-java"}),'import java.time.Duration;\nimport com.linecorp.armeria.common.sse.ServerSentEvent;\nimport com.linecorp.armeria.server.Server;\nimport com.linecorp.armeria.server.streaming.ServerSentEvents;\nimport reactor.core.publisher.Flux;\n\nServer server =\n        Server.builder()\n              // This service infinitely sends numbers as the data of events every second.\n              .service("/long-sse", (ctx, req) -> {\n                  // Note that you MUST adjust the request timeout if you want to send events for a\n                  // longer period than the configured request timeout. The timeout can be disabled by\n                  // setting 0 like the below, but it is NOT RECOMMENDED in the real world application,\n                  // because it can leave a lot of unfinished requests.\n                  ctx.setRequestTimeout(Duration.ZERO);\n                  return ServerSentEvents.fromPublisher(\n                          Flux.interval(Duration.ofSeconds(1))\n                              .map(sequence -> ServerSentEvent.ofData(Long.toString(sequence))));\n              })\n              .build();\n')))}m.isMDXComponent=!0},xCMr:function(e,t,r){"use strict";var n=r("Wbzz"),a=r("q1tI"),s=r.n(a),i=r("/94A"),o=r("+ejy");t.a=function(e){var t=Object(n.useStaticQuery)("1217743243").allMdx.nodes;return s.a.createElement(o.a,Object.assign({},e,{candidateMdxNodes:t,index:i,prefix:"docs",pageTitleSuffix:"Armeria documentation"}))}}}]);
//# sourceMappingURL=component---src-pages-docs-server-sse-mdx-778948d589ab8864e12b.js.map