使用coap协议，传输 字符串，图片，视频，并对外暴漏接口。




coap协议文档
https://tools.ietf.org/html/rfc7252
dtls 介绍文档
https://blog.csdn.net/pengkunlun_hit/article/details/52177227

2. 开源实现

维基百科：http://en.wikipedia.org/wiki/Constrained_Application_Protocol

其中两个开源版本：libcoap（C语言实现）和 Californium（java语言实现），比较实用。

思路
1、使用线程池，等待发送的消息进队。
2、初始化，可以设置线程池中线程的数量
3、初始化完成后，一直轮询消息列表中的的信息。有就发送，没有就等待。

4、但是，作为client，有必要么？



新思路
1、client 要有多个，分别指向不同的server
2、对外提供两种模式，一种是：疯狂发送消息，不需要返回结果
另一种是：发送消息后，等到返回值，返回后，再进行下一次发送


基于CoAP传输的消息应同基于其他协议传输一样，要实现某一接口，并具有同样的格式。

coap消息体中应包含
from ,to, 内容，发送时间，以及一个唯一标识(不能使用时间戳)
发送消息采用回调函数的方式，进行下一步函数的执行。



