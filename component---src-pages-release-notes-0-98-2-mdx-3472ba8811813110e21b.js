(window.webpackJsonp=window.webpackJsonp||[]).push([[79],{"1lec":function(e){e.exports=JSON.parse('{"/release-notes/0.99.9":"v0.99.9","/release-notes/0.99.8":"v0.99.8","/release-notes/0.99.7":"v0.99.7","/release-notes/0.99.6":"v0.99.6","/release-notes/0.99.5":"v0.99.5","/release-notes/0.99.4":"v0.99.4","/release-notes/0.99.3":"v0.99.3","/release-notes/0.99.2":"v0.99.2","/release-notes/0.99.1":"v0.99.1","/release-notes/0.99.0":"v0.99.0","/release-notes/0.98.7":"v0.98.7","/release-notes/0.98.6":"v0.98.6","/release-notes/0.98.5":"v0.98.5","/release-notes/0.98.4":"v0.98.4","/release-notes/0.98.3":"v0.98.3","/release-notes/0.98.2":"v0.98.2"}')},"2+3N":function(e){e.exports=JSON.parse('{"/news/20200703-newsletter-1":"Armeria Newsletter vol. 1","/news/20200514-newsletter-0":"Armeria Newsletter vol. 0"}')},JkCF:function(e,t,a){"use strict";a("tU7J");var n=a("wFql"),i=a("q1tI"),s=a.n(i),r=a("2+3N"),c=a("1lec"),l=a("+ejy"),b=a("+9zj"),o=n.a.Title;t.a=function(e){var t={},a={},n={root:{"Latest news items":"/news","Latest release notes":"/release-notes","Past news items":"/news/list","Past release notes":"/release-notes/list"},"Recent news items":t,"Recent releases":a};Object.entries(r).forEach((function(e){var a=e[0],n=e[1];t[n]=a})),Object.entries(c).forEach((function(e){var t=e[0],n=e[1];a[n]=t}));var i=Object(b.a)(e.location),h=e.version||i.substring(i.lastIndexOf("/")+1);return h.match(/^[0-9]/)||(h=void 0),s.a.createElement(l.a,Object.assign({},e,{candidateMdxNodes:[],index:n,prefix:"release-notes",pageTitle:h?h+" release notes":e.pageTitle,pageTitleSuffix:"Armeria release notes"}),h?s.a.createElement(o,{id:"release-notes",level:1},s.a.createElement("a",{href:"#release-notes","aria-label":"release notes permalink",className:"anchor before"},s.a.createElement("svg",{"aria-hidden":"true",focusable:"false",height:"16",version:"1.1",viewBox:"0 0 16 16",width:"16"},s.a.createElement("path",{fillRule:"evenodd",d:"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"}))),h," release notes"):"",e.children)}},WyTA:function(e,t,a){"use strict";a.r(t),a.d(t,"_frontmatter",(function(){return c})),a.d(t,"default",(function(){return h}));var n,i=a("8o2o"),s=(a("q1tI"),a("7ljp")),r=a("JkCF"),c={},l=(n="ThankYou",function(e){return console.warn("Component "+n+" was not imported, exported, or provided by MDXProvider as global scope"),Object(s.b)("div",e)}),b={_frontmatter:c},o=r.a;function h(e){var t=e.components,a=Object(i.a)(e,["components"]);return Object(s.b)(o,Object.assign({},b,a,{components:t,mdxType:"MDXLayout"}),Object(s.b)("p",{className:"date"},"18th February 2020"),Object(s.b)("h2",{id:"-new-features",style:{position:"relative"}},Object(s.b)("a",Object.assign({parentName:"h2"},{href:"#-new-features","aria-label":" new features permalink",className:"anchor before"}),Object(s.b)("svg",Object.assign({parentName:"a"},{"aria-hidden":"true",focusable:"false",height:"16",version:"1.1",viewBox:"0 0 16 16",width:"16"}),Object(s.b)("path",Object.assign({parentName:"svg"},{fillRule:"evenodd",d:"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"})))),"🌟 New features"),Object(s.b)("ul",null,Object(s.b)("li",{parentName:"ul"},"You can now specify any client options when building with ",Object(s.b)("inlineCode",{parentName:"li"},"ArmeriaRetrofitBuilder"),", because it extends ",Object(s.b)("inlineCode",{parentName:"li"},"AbstractClientOptionsBuilder"),". ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2483"}),"#2483"),Object(s.b)("pre",{parentName:"li"},Object(s.b)("code",Object.assign({parentName:"pre"},{className:"language-java"}),'Retrofit retrofit = ArmeriaRetrofit.builder("http://example.com")\n                                   .factory(...)\n                                   .decorator(...)\n                                   .responseTimeout(...)\n                                   .build();\n')))),Object(s.b)("h2",{id:"-improvements",style:{position:"relative"}},Object(s.b)("a",Object.assign({parentName:"h2"},{href:"#-improvements","aria-label":" improvements permalink",className:"anchor before"}),Object(s.b)("svg",Object.assign({parentName:"a"},{"aria-hidden":"true",focusable:"false",height:"16",version:"1.1",viewBox:"0 0 16 16",width:"16"}),Object(s.b)("path",Object.assign({parentName:"svg"},{fillRule:"evenodd",d:"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"})))),"📈 Improvements"),Object(s.b)("ul",null,Object(s.b)("li",{parentName:"ul"},Object(s.b)("inlineCode",{parentName:"li"},"FallthroughException")," is not a part of the internal API anymore, so you can refer to it when testing your annotation service extensions. ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2495"}),"#2495"))),Object(s.b)("h2",{id:"️-bug-fixes",style:{position:"relative"}},Object(s.b)("a",Object.assign({parentName:"h2"},{href:"#%EF%B8%8F-bug-fixes","aria-label":"️ bug fixes permalink",className:"anchor before"}),Object(s.b)("svg",Object.assign({parentName:"a"},{"aria-hidden":"true",focusable:"false",height:"16",version:"1.1",viewBox:"0 0 16 16",width:"16"}),Object(s.b)("path",Object.assign({parentName:"svg"},{fillRule:"evenodd",d:"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"})))),"🛠️ Bug fixes"),Object(s.b)("ul",null,Object(s.b)("li",{parentName:"ul"},"Armeria clients will not violate the ",Object(s.b)("inlineCode",{parentName:"li"},"MAX_CONCURRENT_STREAMS")," setting enforced by an HTTP/2 server anymore. ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2256"}),"#2256")," ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2374"}),"#2374")),Object(s.b)("li",{parentName:"ul"},"Fixed a regression where ",Object(s.b)("inlineCode",{parentName:"li"},"Server")," fails to read ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/PKCS/armeria/issues/5"}),"PKCS#5")," a private key since 0.98.0 ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2485"}),"#2485")),Object(s.b)("li",{parentName:"ul"},Object(s.b)("inlineCode",{parentName:"li"},"RequestContextExporter")," does not export an entry whose value is ",Object(s.b)("inlineCode",{parentName:"li"},"null")," anymore. ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2492"}),"#2492")),Object(s.b)("li",{parentName:"ul"},Object(s.b)("inlineCode",{parentName:"li"},"DocService")," does not fail with a ",Object(s.b)("inlineCode",{parentName:"li"},"ReflectionsException")," on startup anymore. ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2491"}),"#2491")," ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2494"}),"#2494")),Object(s.b)("li",{parentName:"ul"},"Fixed some potential buffer leaks. ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2497"}),"#2497")," ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2498"}),"#2498")," ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2499"}),"#2499")," ",Object(s.b)("a",Object.assign({parentName:"li"},{href:"https://github.com/line/armeria/issues/2500"}),"#2500"))),Object(s.b)("h2",{id:"-dependencies",style:{position:"relative"}},Object(s.b)("a",Object.assign({parentName:"h2"},{href:"#-dependencies","aria-label":" dependencies permalink",className:"anchor before"}),Object(s.b)("svg",Object.assign({parentName:"a"},{"aria-hidden":"true",focusable:"false",height:"16",version:"1.1",viewBox:"0 0 16 16",width:"16"}),Object(s.b)("path",Object.assign({parentName:"svg"},{fillRule:"evenodd",d:"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"})))),"⛓ Dependencies"),Object(s.b)("ul",null,Object(s.b)("li",{parentName:"ul"},"Brave 5.9.4 → 5.9.5"),Object(s.b)("li",{parentName:"ul"},"gRPC 1.27.0 → 1.27.1",Object(s.b)("ul",{parentName:"li"},Object(s.b)("li",{parentName:"ul"},"Protobuf 3.11.3 → 3.11.4"))),Object(s.b)("li",{parentName:"ul"},"Micrometer 1.3.3 → 1.3.5"),Object(s.b)("li",{parentName:"ul"},"Tomcat 9.0.30 → 9.0.31, 8.5.50 → 8.5.51"),Object(s.b)("li",{parentName:"ul"},"ZooKeeper 3.5.6 → 3.5.7"),Object(s.b)("li",{parentName:"ul"},"Shaded dependencies:",Object(s.b)("ul",{parentName:"li"},Object(s.b)("li",{parentName:"ul"},"fastutil 8.3.0 → 8.3.1")))),Object(s.b)("h2",{id:"-thank-you",style:{position:"relative"}},Object(s.b)("a",Object.assign({parentName:"h2"},{href:"#-thank-you","aria-label":" thank you permalink",className:"anchor before"}),Object(s.b)("svg",Object.assign({parentName:"a"},{"aria-hidden":"true",focusable:"false",height:"16",version:"1.1",viewBox:"0 0 16 16",width:"16"}),Object(s.b)("path",Object.assign({parentName:"svg"},{fillRule:"evenodd",d:"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"})))),"🙇 Thank you"),Object(s.b)(l,{usernames:["anuraaga","ikhoon","fukuang","jrhee17","mauhiz","minwoox","themnd","trustin","yuinacor"],mdxType:"ThankYou"}))}h.isMDXComponent=!0}}]);
//# sourceMappingURL=component---src-pages-release-notes-0-98-2-mdx-3472ba8811813110e21b.js.map