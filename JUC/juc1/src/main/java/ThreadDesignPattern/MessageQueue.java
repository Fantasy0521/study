package ThreadDesignPattern;

import java.util.LinkedList;

/**
 * 消息队列模式 java线程之间通信，不是RabbitMQ这种进程间的通信
 */
public class MessageQueue {

    //消息的队列集合
    private LinkedList<Message> list = new LinkedList<>();

    //消息队列的容量
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    //获取消息的方法
    public Message take(){
        synchronized (list){
            // 检查队列是否为空
            while (list.isEmpty()){
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //从队列的头部返回消息
        Message message = list.removeFirst();
        list.notifyAll();
        return message;
    }

    //存入消息
    public void put(Message message){
        synchronized (list){
            //检查队列是否已满
            while (list.size() == capacity){
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            //从尾部加
            list.addLast(message);
            list.notifyAll();
        }
    }

}

final class Message{
    private int id;
    private Object value;

    public int getId(){
        return id;
    }

    public Object getValue() {
        return value;
    }

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
