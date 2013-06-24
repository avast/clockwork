<map version="0.9.0">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1371475680607" ID="ID_23789842" MODIFIED="1371816412733" TEXT="Coroutines">
<node CREATED="1371475751067" FOLDED="true" ID="ID_1245768683" MODIFIED="1371816451341" POSITION="right" TEXT="Streaming M/R (StreamReduce)">
<node CREATED="1371552140594" FOLDED="true" ID="ID_1263871368" MODIFIED="1371816407639" TEXT="MR semantics for stream processing">
<node CREATED="1371653512899" ID="ID_1643070960" MODIFIED="1371654050125" TEXT="Traditional MR is PULL">
<node CREATED="1371653860002" ID="ID_534873837" MODIFIED="1371653862630" TEXT="Offline"/>
<node CREATED="1371653771335" ID="ID_1735270840" MODIFIED="1371653866958" TEXT="Batch"/>
</node>
<node CREATED="1371653473242" ID="ID_167218712" MODIFIED="1371653484365" TEXT="MapReduce paradigm">
<node CREATED="1371654668725" ID="ID_1084738801" MODIFIED="1371654678095" TEXT="inspired by functional programming"/>
<node CREATED="1371654681159" ID="ID_1331960668" MODIFIED="1371654687306" TEXT="very old concept"/>
</node>
<node CREATED="1371654188859" ID="ID_459962329" MODIFIED="1371743360429" TEXT="Benefits">
<node CREATED="1371654212046" ID="ID_284964959" MODIFIED="1371654234619" TEXT="Scalable">
<node CREATED="1371654248718" ID="ID_159007509" MODIFIED="1371654255550" TEXT="vertically"/>
<node CREATED="1371654256006" ID="ID_1183272394" MODIFIED="1371654260220" TEXT="horizontally"/>
<node CREATED="1371654371695" ID="ID_1670876719" MODIFIED="1371654455519" TEXT="mappers can be torn away from reducers"/>
</node>
<node CREATED="1371654235604" ID="ID_1012677564" MODIFIED="1371654347607" TEXT="Simple computational model"/>
<node CREATED="1371654469593" ID="ID_722453129" MODIFIED="1371654594402" TEXT="Algorithms">
<node CREATED="1371654537143" ID="ID_675366157" MODIFIED="1371654557243" TEXT="BigData analytics"/>
<node CREATED="1371654520431" ID="ID_1298817752" MODIFIED="1371654563577" TEXT="Machine learning"/>
</node>
</node>
<node CREATED="1371475886093" ID="ID_721011708" MODIFIED="1371653428874" TEXT="WordCount">
<node CREATED="1371552334280" ID="ID_658230676" MODIFIED="1371654740245" TEXT="The introductory MR job in Hadoop"/>
</node>
<node CREATED="1371655547769" ID="ID_1880592763" MODIFIED="1371655778618" TEXT="Can we write streaming apps (PUSH) like PULL?">
<node CREATED="1371655627646" ID="ID_852138274" MODIFIED="1371655649658" TEXT="It could inherit the benefits of traditional MR"/>
</node>
</node>
<node CREATED="1371721042517" FOLDED="true" ID="ID_1723551625" MODIFIED="1371816406482" TEXT="Clockwork">
<node CREATED="1371721191617" ID="ID_334390887" MODIFIED="1371721196687" TEXT="Intro">
<node CREATED="1371721251711" ID="ID_984792635" MODIFIED="1371721257698" TEXT="Execution model">
<node CREATED="1371721513378" ID="ID_1562939681" MODIFIED="1371722003184" TEXT="wheels, funnels, tanks"/>
<node CREATED="1371733426456" ID="ID_737660719" MODIFIED="1371733441095" TEXT="Type safety between transformers"/>
</node>
</node>
<node CREATED="1371720919576" ID="ID_1757612397" MODIFIED="1371721054448" TEXT="Word-Count">
<node CREATED="1371720936011" ID="ID_1811423805" MODIFIED="1371720993823" TEXT="one map, one reduce"/>
<node CREATED="1371723062708" ID="ID_418030116" MODIFIED="1371723068699" TEXT="several configurations">
<node CREATED="1371723069044" ID="ID_1195548333" MODIFIED="1371723073979" TEXT="single node"/>
<node CREATED="1371723076683" ID="ID_59474020" MODIFIED="1371723085131" TEXT="distributed">
<node CREATED="1371723094054" ID="ID_1637090880" MODIFIED="1371723111308" TEXT="no combiner"/>
<node CREATED="1371723112726" ID="ID_461795165" MODIFIED="1371723114683" TEXT="combiner"/>
<node CREATED="1371723117229" ID="ID_177768531" MODIFIED="1371723121299" TEXT="more reducers"/>
</node>
</node>
</node>
<node CREATED="1371720865784" ID="ID_1602106769" MODIFIED="1371723300424" TEXT="&quot;Traditional&quot; Pipeline example">
<attribute_layout NAME_WIDTH="52" VALUE_WIDTH="33"/>
<node CREATED="1371720888515" ID="ID_815843599" MODIFIED="1371721023134" TEXT="many maps, no reducers"/>
</node>
<node CREATED="1371475859482" ID="ID_1721344355" MODIFIED="1371552160403" TEXT="The &quot;Nerdiest&quot; Clock in the world">
<node CREATED="1371652898813" ID="ID_1319835995" MODIFIED="1371721018254" TEXT="many reducers, no maps"/>
</node>
<node CREATED="1371475922400" ID="ID_425418757" MODIFIED="1371652932985" TEXT="Merger">
<node CREATED="1371552421894" ID="ID_1417287252" MODIFIED="1371552469934" TEXT="Universal record merger"/>
<node CREATED="1371552471601" ID="ID_1980158413" MODIFIED="1371552507851" TEXT="Comparison traditional vs. clockwork (coroutine)"/>
</node>
<node CREATED="1371551729042" ID="ID_624878245" MODIFIED="1371721079268" TEXT="Machine Learning">
<node CREATED="1371475931923" ID="ID_1894311337" MODIFIED="1371551735786" TEXT="Naive-Bayes Classification">
<node CREATED="1371552270208" ID="ID_1399711297" MODIFIED="1371552309529" TEXT="Hadoop solution (Blog article)"/>
<node CREATED="1371552310500" ID="ID_1640352664" MODIFIED="1371552322290" TEXT="Clockwork solution"/>
</node>
</node>
</node>
<node CREATED="1371552663861" FOLDED="true" ID="ID_92431836" MODIFIED="1371816409038" TEXT="Stream MR Idiosyncrazies">
<node CREATED="1371552789896" ID="ID_1104694915" MODIFIED="1371580247178" TEXT="Infinite influx">
<node CREATED="1371741342208" ID="ID_1898400358" MODIFIED="1371741348623" TEXT="No EOF"/>
<node CREATED="1371741355101" ID="ID_1857594154" MODIFIED="1371741367132" TEXT="limited resources">
<node CREATED="1371741368037" ID="ID_1693121437" MODIFIED="1371741370467" TEXT="memory"/>
<node CREATED="1371741435174" ID="ID_112739060" MODIFIED="1371741442230" TEXT="nw bandwith"/>
</node>
<node CREATED="1371741400822" ID="ID_658004131" MODIFIED="1371741428429" TEXT="accumulation, flushing"/>
</node>
<node CREATED="1371553315048" ID="ID_1444484438" MODIFIED="1371741392454" TEXT="Flushing strategies">
<node CREATED="1371552861029" ID="ID_9169647" MODIFIED="1371553708456" TEXT="Up-to-dateness vs. bandwith">
<node CREATED="1371553657454" ID="ID_1952872115" MODIFIED="1371553688729" TEXT="Up-to-dateness"/>
<node CREATED="1371553689156" ID="ID_246676850" MODIFIED="1371553691760" TEXT="Bandwith"/>
</node>
<node CREATED="1371553369388" ID="ID_409455486" MODIFIED="1371553621865">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <p>
      If the mappers do not flush frequently
    </p>
    <p>
      on reducers&#160;we can afford
    </p>
    <p>
      long-lasting (ACID) transacitions.
    </p>
    <p>
      
    </p>
    <p>
      Mappers = big data world
    </p>
    <p>
      Reducers = small data world (a few nodes, small throughput)
    </p>
  </body>
</html></richcontent>
</node>
</node>
<node CREATED="1371554253806" ID="ID_1482419657" MODIFIED="1371741490705" TEXT="Incremental models">
<node CREATED="1371554499595" ID="ID_291188585" MODIFIED="1371554514303" TEXT="processing the request must be quick"/>
<node CREATED="1371554312016" ID="ID_1173127571" MODIFIED="1371554525819" TEXT="discriminatory for some MR algorithms">
<node CREATED="1371554298127" ID="ID_211517254" MODIFIED="1371582141233" TEXT="convergence loops"/>
</node>
</node>
</node>
</node>
<node CREATED="1371475806712" FOLDED="true" ID="ID_41108419" MODIFIED="1371816414317" POSITION="right" TEXT="Conclusion">
<node CREATED="1371582159226" FOLDED="true" ID="ID_669859403" MODIFIED="1371816410262" TEXT="Work To Be Done (with examples)">
<node CREATED="1371647998298" ID="ID_1022824698" MODIFIED="1371648007550" TEXT="Bag Of Words"/>
<node CREATED="1371552764839" ID="ID_693392639" MODIFIED="1371582173398" TEXT="Sliding statistics (mean, deviation)"/>
<node CREATED="1371577420363" ID="ID_1557950472" MODIFIED="1371582176526" TEXT="Heavy-Hitters problem">
<node CREATED="1371572772786" ID="ID_1199199834" MODIFIED="1371577481144" TEXT="SpaceSavings">
<node CREATED="1371568239282" ID="ID_1498044996" MODIFIED="1371582233137" TEXT="decay function optimization"/>
</node>
<node CREATED="1371565617213" ID="ID_1575958245" MODIFIED="1371577445123" TEXT="ADWIN">
<node CREATED="1371567164052" ID="ID_1464568200" MODIFIED="1371567178725" TEXT="replacing counters by estimators"/>
</node>
<node CREATED="1371582241199" ID="ID_879171904" MODIFIED="1371582255361" TEXT="Kalman filters"/>
</node>
<node CREATED="1371582089733" ID="ID_1255167629" MODIFIED="1371582180595" TEXT="Clustering">
<node CREATED="1371582094363" ID="ID_1410487506" MODIFIED="1371582099564" TEXT="Cobweb">
<node CREATED="1371582104021" ID="ID_1292410566" MODIFIED="1371582321689" TEXT="incremental conceptual clustering"/>
<node CREATED="1371650769449" ID="ID_1144726608" MODIFIED="1371650771197" TEXT="WEKA"/>
</node>
<node CREATED="1371647124747" ID="ID_793210333" MODIFIED="1371647127120" TEXT="k-means"/>
</node>
<node CREATED="1371650779659" ID="ID_723564829" MODIFIED="1371650784047" TEXT="Classification">
<node CREATED="1371645205617" ID="ID_1011100237" MODIFIED="1371650786115" TEXT="Support Vector Machine"/>
<node CREATED="1371650998745" ID="ID_268547435" MODIFIED="1371651002341" TEXT="Naive-Bayes">
<node CREATED="1371650787743" ID="ID_1661436910" MODIFIED="1371651007608" TEXT="WEKA"/>
<node CREATED="1371651017684" ID="ID_1954549916" MODIFIED="1371651025277" TEXT="Clockwork"/>
</node>
</node>
<node CREATED="1371650170431" ID="ID_1568922610" MODIFIED="1371650174377" TEXT="WEKA"/>
<node CREATED="1371660686198" ID="ID_1307794979" MODIFIED="1371660701651" TEXT="Text Stream Processing">
<node CREATED="1371660701654" ID="ID_315051746" MODIFIED="1371660769302" TEXT="picture from page 62 in http://www.slideshare.net/mgrcar/text-and-text-stream-mining-tutorial-15137759"/>
</node>
</node>
<node CREATED="1371567287605" ID="ID_13195674" MODIFIED="1371651371393" TEXT="References"/>
</node>
<node CREATED="1371475730238" ID="ID_1920909013" MODIFIED="1371551493423" POSITION="left" TEXT="Intro">
<node CREATED="1371826160162" ID="ID_479115080" MODIFIED="1371826168251" TEXT="Introductory example">
<node CREATED="1371826169033" ID="ID_1410186192" MODIFIED="1371826176627" TEXT="JoinedIterator"/>
<node CREATED="1371826177406" ID="ID_1106850949" MODIFIED="1371826188062" TEXT="PipeJoinedIterator"/>
<node CREATED="1371826188575" ID="ID_564442168" MODIFIED="1371826194547" TEXT="CoJoinedIterator">
<node CREATED="1371826207311" ID="ID_557491829" MODIFIED="1371826221113" TEXT="Mathiass Mann&apos;s library"/>
</node>
</node>
<node CREATED="1371476029070" ID="ID_133452635" MODIFIED="1371826199944" TEXT="Concept and History">
<node CREATED="1371476374766" ID="ID_1043964632" MODIFIED="1371476402851" TEXT="subroutine generalization">
<node CREATED="1371478384919" ID="ID_1093734837" MODIFIED="1371478389034" TEXT="aka green threads"/>
<node CREATED="1371827309096" ID="ID_1803318031" MODIFIED="1371827314605" TEXT="like subroutines"/>
<node CREATED="1371827315360" ID="ID_1717825035" MODIFIED="1371827341888" TEXT="coroutines can call other coroutines"/>
<node CREATED="1371827342835" ID="ID_1184013049" MODIFIED="1371827420665" TEXT="their execution can later return to the point of calling the other coroutine"/>
</node>
<node CREATED="1371826257778" ID="ID_1451641184" MODIFIED="1371827228155" TEXT="solves Producer/Consumer problem">
<node CREATED="1371826276498" ID="ID_186130360" MODIFIED="1371827235677" TEXT="example from wiki"/>
</node>
<node CREATED="1371476358740" ID="ID_1167520242" MODIFIED="1371476374082" TEXT="stetful switching between subroutines"/>
<node CREATED="1371476528267" ID="ID_1183198889" MODIFIED="1371827723766" TEXT="language support (Ruby, Go, Lua)">
<node CREATED="1371476918493" ID="ID_841337873" MODIFIED="1371476937932" TEXT="none of the top 5 of the TIOBE"/>
</node>
</node>
<node CREATED="1371476572143" ID="ID_283371387" MODIFIED="1371651429655" TEXT="And what about Java?">
<node CREATED="1371476584833" ID="ID_594197684" MODIFIED="1371476597822" TEXT="Emulation by threads">
<node CREATED="1371476598314" ID="ID_331811125" MODIFIED="1371476643907" TEXT="Ugly"/>
</node>
<node CREATED="1371476617074" ID="ID_971789765" MODIFIED="1371476624371" TEXT="Byte code transformation">
<node CREATED="1371476631467" ID="ID_1140748473" MODIFIED="1371476637612" TEXT="Clumsy"/>
</node>
<node CREATED="1371476660261" ID="ID_1590089287" MODIFIED="1371476668829" TEXT="A need for a JSR!">
<node CREATED="1371827758966" ID="ID_440442939" MODIFIED="1371827760483" TEXT="ideally"/>
</node>
<node CREATED="1371476033404" ID="ID_1571430514" MODIFIED="1371827810198" TEXT="Severall Java Co projects">
<node CREATED="1371476196707" ID="ID_4941531" MODIFIED="1371476198006" TEXT="http://ssw.jku.at/General/Staff/LS/coro/"/>
<node CREATED="1371476276592" ID="ID_930463177" MODIFIED="1371476277964" TEXT="http://ssw.jku.at/General/Staff/LS/coro/CoroIntroduction.pdf"/>
</node>
</node>
<node CREATED="1371476986778" ID="ID_1035228669" MODIFIED="1371651428197" TEXT="How does it work?">
<node CREATED="1371479279457" ID="ID_1514457152" MODIFIED="1371653556150" TEXT="Demonstration">
<node CREATED="1371479405828" ID="ID_394264255" MODIFIED="1371479637977">
<richcontent TYPE="NODE"><html>
  <head>
    
  </head>
  <body>
    <p>
      A simple Producer/Consumer loop
    </p>
    <ol>
      <li>
        Printing the execution in both the producer and consumer
      </li>
      <li>
        Illustrate the exec flow on the console
      </li>
    </ol>
  </body>
</html></richcontent>
</node>
</node>
</node>
<node CREATED="1371479297393" ID="ID_1446721497" MODIFIED="1371651417351" TEXT="More info">
<node CREATED="1371478880255" ID="ID_1020776705" MODIFIED="1371479680788" TEXT="Types">
<node CREATED="1371478888396" ID="ID_1610319176" MODIFIED="1371478905504" TEXT="symmetric">
<node CREATED="1371478910152" ID="ID_538290075" MODIFIED="1371479038921" TEXT="no caller, single-thread multitasking, must be scheduled"/>
</node>
<node CREATED="1371478891354" ID="ID_355356718" MODIFIED="1371478908998" TEXT="asymmetric">
<node CREATED="1371478994938" ID="ID_1695477465" MODIFIED="1371479046245" TEXT="producer/consumer, no scheduling"/>
</node>
</node>
<node CREATED="1371478716509" ID="ID_107542678" MODIFIED="1371479336513" TEXT="Thread affinity"/>
<node CREATED="1371479181184" ID="ID_878043833" MODIFIED="1371479340561" TEXT="Migration"/>
<node CREATED="1371479186054" ID="ID_497179237" MODIFIED="1371479343566" TEXT="Serialization"/>
</node>
<node CREATED="1371476058271" ID="ID_793256443" MODIFIED="1371553625689" TEXT="Advantages, highlights">
<node CREATED="1371476325975" ID="ID_664496944" MODIFIED="1371476336055" TEXT="no race conditions"/>
</node>
</node>
<node CREATED="1371475740928" ID="ID_376540945" MODIFIED="1371651437529" POSITION="left" TEXT="Basic Examples">
<node CREATED="1371475953792" ID="ID_1970349511" MODIFIED="1371475962513" TEXT="Producer/Consumer"/>
<node CREATED="1371476105201" ID="ID_1900053174" MODIFIED="1371476122301" TEXT="Loop/Iterator conversions"/>
<node CREATED="1371476744552" ID="ID_371863639" MODIFIED="1371476843506" TEXT="Event loop (vs. callback)"/>
<node CREATED="1371477067519" ID="ID_461580830" MODIFIED="1371477074416" TEXT="Non-blocking server impl"/>
</node>
<node CREATED="1371653928256" ID="ID_209489631" MODIFIED="1371653934530" POSITION="left" TEXT="Summary">
<node CREATED="1371653939011" ID="ID_1130521053" MODIFIED="1371653958577" TEXT="Pull -&gt; Push conversion">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1371653966773" ID="ID_1944069406" MODIFIED="1371816520549" TEXT="Allows writing Push algorithms in Pull way"/>
</node>
</node>
</map>
