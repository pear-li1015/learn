关于 coap client 和 server 的一点问题
1、目前采用client 发送， server接收。
server不response，client不接收 server 的 response
2、也可以进行修改，使client可以接收 response，server发送 response。
但这种方式存在一些弊端。
3、弊端：在多个client同时工作的情况下， 如果client的空闲率太高，应该考虑关闭部分client
就在关闭client时，如果client向server发送了请求，server的响应还没有被client接收，
就关闭了此client，结果当然是数据丢失。
4、目前仍维持第一条中的方式，在开发完毕后，看情况，是否要对此种方式的效率进行测试。
当然一定要考虑一种解决方案，规避掉数据丢失的情况



反观
1、目前这种client server的方式也存在一些问题。
如：某台物理设备上，只有client没有server，这样，我们就不能用服务器上的client 响应设备的请求。


想法
1、可以采用两者结合的方式进行，
对于 不含有server的设备，使用设备的client接收响应。这种设备的client 应该是有且仅有一个而且不会关闭的。
也就不存在丢数据的情况。
对于有 server的设备，使用服务器的client向设备的server发送响应。
需测试通信的效率。
2、这样的话，就需要做一些事情，比如说Message携带的信息。
除了指明哪种协议发送的。还要指出，是否含有server
3、每一个设备要有一个协议地址。除了声明使用哪种通信协议通信，还要告诉服务器的client如何找到此设备。
当然，如果此设备无法主动接收请求的话就算了。
这两点，要存在影子里。

一个小问题
DTLSConnector一次最多发送 16384字节的数据，
需要对CoAPMessage进行进一步封装，最外层应包含：帧号，总帧数，最新帧传入时间。。。
还应增加一线程持续扫描等待回调的message，以便进行超时重发。
当然也要有最大超时时间。


一点点担忧
https://baijiahao.baidu.com/s?id=1609055547851599818&wfr=spider&for=pc
上文中指出，在资源非常少的终端和极有限的带宽下使用dtls加密的coap可能会跑不起来。


每帧应该包含以下内容
1、协议类型 1byte
2、回调函数相关状态 1byte
3、发送方、接收方 暂定各12byte
4、发送时间  new Date().getTime() 13byte long 占8byte
5、uuid 32byte
6、当前帧号 int 4byte
7、总共帧数。
1-7 为帧头。
8、消息体。
还可以包含 差错控制编码（链路层）、优先级、安全控制 等信息。

应具有的功能
1、超时重发
有一线程一直在扫等待接收数据的message， 如果太长时间一直没有新的帧传入，重新请求未收到的帧。
2、只具有client的设备，在使用dtls加密的协议请求大文件时。
server.response无法一次返回全部内容，需要client多次请求。

需要保证线程安全的地方
1、list.add 或 list.addall
2、list.get(0) remove(0)
3、判断list的长度。一般情况是先判断list的长度是否大于0，然后get(0)
以上三点使用 sychnolized即可保证。


超时重发的一点思考
1、只有client
使用coap自带的超时重发
2、有client和server
client若收到，进行ack，server若超时未收到ack则重发。
3、以上，只有server在收到ack后，才能释放缓存资源。