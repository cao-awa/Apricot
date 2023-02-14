# Base256 
Base256位于``` com.github.cao.awa.apricot.mathematic.base.Base256.java ```

此类用于将数字转换为byte数组，不兼容 ``` SimInt ``` 的数字（因为SimInt本身就可以直接转字节数组）

## 用法 
```java
// 将long转换为byte数组（8字节）
byte[] buf = Base256.longToBuf(aLong);
// 或者
Base256.longToBuf(aLong, buf);

// 将byte数组转换为long
long aLong = Base256.longFromBuf(buf);


// 将int转换为byte数组（4字节）
byte[] buf = Base256.intToBuf(aInt);
// 或者
Base256.intToBuf(aInt, buf);

// 将byte数组转换为int
int aInt = Base256.intFromBuf(buf);


// 将short转换为byte数组（2字节）
byte[] buf = Base256.tagToBuf(aInt);
// 或者
Base256.tagToBuf(aInt, buf);

// 将byte数组转换为short
int aInt = Base256.tagFromBuf(buf);
```

## 实现
```java
public static byte[] longToBuf(long l, byte[] buf) {
    buf[0] = (byte) (l >>> 56);
    buf[1] = (byte) (l >>> 48);
    buf[2] = (byte) (l >>> 40);
    buf[3] = (byte) (l >>> 32);
    buf[4] = (byte) (l >>> 24);
    buf[5] = (byte) (l >>> 16);
    buf[6] = (byte) (l >>> 8);
    buf[7] = (byte) (l);
    return buf;
}

public static long longFromBuf(byte[] buf) {
    return ((buf[0] & 0xFFL) << 56) +
           ((buf[1] & 0xFFL) << 48) +
           ((buf[2] & 0xFFL) << 40) +
           ((buf[3] & 0xFFL) << 32) +
           ((buf[4] & 0xFFL) << 24) +
           ((buf[5] & 0xFFL) << 16) +
           ((buf[6] & 0xFFL) << 8) +
           ((buf[7] & 0xFFL));
}
```

在转为字节数组时（longToBuf），将一个数字的指定部分（8bit一组）右移至最高位，强制转换为byte时只会保留那8位

在还原时（longFromBuf），将各个字节转为long类型，然后左移到原先的位置后全部相加，即可得到原数字

## 优势
### 长度固定
使用这种方式存储数字，具有长度固定的优势，可以直接通过seek到指定倍数下标读取数字

极大加快顺序存取的速度，降低链表成本（如有序的消息id列表）

### 更小
使用这种方式存储的long，在数字超过99999999时，具有更小的存储优势

使用这种方式存储的int，在数字超过9999时，具有更小的存储优势

\
而要使得Base256在更小时也存在存储优势，请参考 ``` MessageStore ``` 的可变长编码

这会导致长度固定的优势失效

## 注意
不要对Base256生成的数据进行任何编码（如UTF-8、GBK）这会导致数据损坏且不可逆！
