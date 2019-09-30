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