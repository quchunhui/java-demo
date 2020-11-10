package api.demo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.TransactionBlock;

public class RedisApiDemo {
    private static Jedis jedis = null;

    public static void main(String[] args) {
        RedisApiDemo client = RedisApiDemo.getInstance();
        client.set("mykey", "myvalue");
        System.out.println(client.get("mykey"));
        client.set("mykey", "myvalue1");
        System.out.println(client.get("mykey"));
    }

    /**
     * 构造函数
     */
    private RedisApiDemo() {
        if (jedis == null) {
            jedis = new Jedis("192.168.29.100", 6380);
        }
    }

    /**
     * 单例模式
     */
    private static class SingletonInstance {
        private static final RedisApiDemo INSTANCE = new RedisApiDemo();
    }

    /**
     * 获取对象句柄
     */
    private static RedisApiDemo getInstance() {
        return SingletonInstance.INSTANCE;
    }

    /**
     * Redis基本（键）操作
     * 删除
     * @return 被删除 key 的数量
     */
    public Long delete(String... keys){
        return jedis.del(keys);
    }

    /**
     * Redis基本（键）操作
     * 返回 key 所储存的值的类型。
     * @return none(key不存在)，string(字符串)，list(列表)，set(集合)，zset(有序集)，hash(哈希表)
     */
    public String type(String key){
        return jedis.type(key);
    }

    /**
     * Redis基本（键）操作
     * 获取键列表
     */
    public Set<String> keys(String pattern){
        return jedis.keys(pattern);
    }

    /**
     * Redis基本（键）操作
     * 查看某个key的剩余生存时间:time to live
     * @return 当 key 不存在或没有设置生存时间时，返回 -1 。否则，返回 key 的剩余生存时间(以秒为单位)。
     */
    public Long ttl(String key){
        return jedis.ttl(key);
    }

    /**
     * Redis基本（键）操作
     * 查看某个key是否存在
     */
    public Boolean exists(String key){
        return jedis.exists(key);
    }

    /**
     * Redis基本（键）操作
     * 设置某一记录的生存时间，过期删除
     * @param key 设置可挥发的key
     * @param seconds 超时时间，单位：秒
     * @return 结果
     */
    public Long expire(String key, int seconds){
        return jedis.expire(key, seconds);
    }

    /**
     * Redis基本（键）操作
     * 移除给定 key 的生存时间，将这个 key从可挥发的(带生存时间 key )转换成持久化的(一个不带生存时间、永不过期的 key )
     */
    public Long persist(String key){
        return jedis.persist(key);
    }

    /**
     * Redis基本（字符串）操作
     * 将设置某个key的值，如果 key 已经持有其他值，就覆写旧值，无视类型。
     */
    public String set(String key, String value){
        return jedis.set(key, value);
    }

    /**
     * Redis基本（字符串）操作
     * 将 key 的值设为 value ，当且仅当 key不存在，若给定的 key 已经存在，则 SETNX 不做任何动作。
     * set not exists
     */
    public Long setnx(String key, String value){
        return jedis.setnx(key, value);
    }

    /**
     * Redis基本（字符串）操作
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
     */
    public String setex(String key, int seconds, String value){
        return jedis.setex(key, seconds, value);
    }

    /**
     * Redis基本（字符串）操作
     * 返回 key 所关联的字符串值。
     */
    public String get(String key){
        return jedis.get(key);
    }

    /**
     * Redis基本（字符串）操作
     * 将 key 中储存的数字值增一。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     */
    public Long incr(String key){
        return jedis.incr(key);
    }

    /**
     * Redis基本（字符串）操作
     * 将 key 中储存的数字值增加固定制。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     */
    public Long incrBy(String key, long increase){
        return jedis.incrBy(key, increase);
    }

    /**
     * Redis基本（字符串）操作
     * 将 key 中储存的数字值减一。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     */
    public Long decr(String key){
        return jedis.decr(key);
    }

    /**
     * Redis基本（字符串）操作
     * 将 key 中储存的数字值减少固定制。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     */
    public Long decrBy(String key, long decrease){
        return jedis.decrBy(key, decrease);
    }

    /**
     * Hash操作
     * 设置字段值
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1；如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
     */
    public Long hset(String hkey, String field, String value){
        return jedis.hset(hkey, field, value);
    }

    /**
     * Hash操作
     * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在
     * 若域 field 已经存在，该操作无效。
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令
     * @return 设置成功，返回 1 。如果给定域已经存在且没有操作被执行，返回 0 。
     */
    public Long hsetnx(String hkey, String field, String value){
        return jedis.hsetnx(hkey, field, value);
    }

    /**
     * Hash操作
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中
     * 此命令会覆盖哈希表中已存在的域
     * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
     * @return 如果命令执行成功，返回 OK
     */
    public String hmset(String hkey, Map<String, String> hash){
        return jedis.hmset(hkey, hash);
    }

    /**
     * Hash操作
     * 返回哈希表 key 中给定域 field 的值。
     * @return 给定域的值
     */
    public String hget(String hkey, String field){
        return jedis.hget(hkey, field);
    }

    /**
     * Hash操作
     * 返回哈希表中，所有的域和值
     * @return 以列表形式返回哈希表的域和域的值
     */
    public Map<String, String> hgetAll(String hkey){
        return jedis.hgetAll(hkey);
    }

    /**
     * Hash操作
     * 删除哈希表中的一个字段，字段不存在将被忽略
     * @return 被成功移除的域的数量，不包括被忽略的域
     */
    public Long hdel(String hkey, String field){
        return jedis.hdel(hkey, field);
    }

    /**
     * Hash操作
     * 返回哈希表中字段的数量
     * @return 哈希表中域的数量，当 key 不存在时，返回 0 。
     */
    public Long hlen(String hkey){
        return jedis.hlen(hkey);
    }

    /**
     * Hash操作
     * 查看哈希表 key 中，给定field 是否存在
     */
    public Boolean hexists(String hkey, String field){
        return jedis.hexists(hkey, field);
    }

    /**
     * Hash操作
     * 为哈希表 key 中的域 field 的值加上增量 increment
     */
    public Long hincrBy(String hkey, String field, long increment){
        return jedis.hincrBy(hkey, field, increment);
    }

    /**
     * Hash操作
     * 返回哈希表 key 中的所有域
     */
    public Set<String> hkeys(String hkey){
        return jedis.hkeys(hkey);
    }

    /**
     * Hash操作
     * 返回哈希表 key 中所有域的值
     */
    public List<String> hvals(String hkey){
        return jedis.hvals(hkey);
    }

    /**
     * List操作
     * 将一个值 value 插入到列表 key 的表头
     * 当 key 存在但不是列表类型时，返回一个错误
     * @return 执行 LPUSH 命令后，列表的长度
     */
    public Long lpush(String lkey, String value){
        return jedis.lpush(lkey, value);
    }

    /**
     * List操作
     * 将一个值 value 插入到列表 key 的表尾(最右边)
     * 当 key 存在但不是列表类型时，返回一个错误
     * @return 执行 RPUSH 操作后，列表的长度
     */
    public Long rpush(String lkey, String value){
        return jedis.rpush(lkey, value);
    }

    /**
     * List操作
     * 移除并返回列表 key 的头元素
     * @return 列表的头元素
     */
    public String lpop(String lkey){
        return jedis.lpop(lkey);
    }

    /**
     * List操作
     * 移除并返回列表 key 的尾元素
     * @return 列表的尾元素
     */
    public String rpop(String lkey){
        return jedis.rpop(lkey);
    }

    /**
     * List操作
     * @return 返回列表的长度
     */
    public Long llen(String lkey){
        return jedis.llen(lkey);
    }

    /**
     * List操作
     * 返回列表中指定区间内的元素，区间以偏移量 start 和 end 指定
     * 也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
     */
    public List<String> lrange(String lkey, int start, int end){
        return jedis.lrange(lkey, start, end);
    }

    /**
     * List操作
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素
     * count = 0 : 移除表中所有与 value 相等的值
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值
     * @return 被移除元素的数量
     */
    public Long lrem(String lkey, int count, String value){
        return jedis.lrem(lkey, count, value);
    }

    /**
     * List操作
     * 将列表 key 下标为 index 的元素的值设置为 value
     */
    public String lset(String lkey, long index, String value){
        return jedis.lset(lkey, index, value);
    }

    /**
     * Set操作
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略
     * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合
     * 当 key 不是集合类型时，返回一个错误
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素
     */
    public Long sadd(String skey, String member){
        return jedis.sadd(skey, member);
    }

    /**
     * Set操作
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略
     * @return 被成功移除的元素的数量，不包括被忽略的元素。
     */
    public Long srem(String skey, String member){
        return jedis.srem(skey, member);
    }

    /**
     * Set操作
     * 返回集合 key 中的所有成员
     * 不存在的 key 被视为空集合
     * @return 集合中的所有成员
     */
    public Set<String> smembers(String skey){
        return jedis.smembers(skey);
    }

    /**
     * Set操作
     * 判断 member 元素是否集合 key 的成员
     * @return 如果 member 元素是集合的成员，返回true
     */
    public Boolean sismember(String skey, String member){
        return jedis.sismember(skey, member);
    }

    /**
     * Set操作
     * 返回集合 key 的基数(集合中元素的数量)
     * @return 集合的基数
     */
    public Long scard(String skey){
        return jedis.scard(skey);
    }

    /**
     * Set操作
     * 移除并返回集合中的一个元素
     * @return 被移除的元素
     */
    public String spop(String skey){
        return jedis.spop(skey);
    }

    /**
     * Set操作
     * 返回一个集合的全部成员，该集合是所有给定集合的交集
     * 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)
     * @return 交集成员的列表
     */
    public Set<String> sinter(String... skeys){
        return jedis.sinter(skeys);
    }

    /**
     * Set操作
     * 返回一个集合的全部成员，该集合是所有给定集合的并集
     * @return 并集成员的列表
     */
    public Set<String> sunion(String... skeys){
        return jedis.sunion(skeys);
    }

    /**
     * Set操作
     * 返回一个集合的全部成员，该集合是所有给定集合之间的差集
     * @return 交集成员的列表
     */
    public Set<String> sdiff(String... skeys){
        return jedis.sdiff(skeys);
    }

    /**
     * Sorted set操作（有序集）
     * 将一个 member 元素及其 score 值加入到有序集 key 当中
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员
     */
    public Long zadd(String zkey, double score, String member){
        return jedis.zadd(zkey, score, member);
    }

    /**
     * Sorted set操作（有序集）
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略
     * 当 key 不是集合类型，返回一个错误
     * @return 被成功移除的元素的数量，不包括被忽略的元素
     */
    public Long zrem(String zkey, String member){
        return jedis.zrem(zkey, member);
    }

    /**
     * Sorted set操作（有序集）
     * 返回有序集 key 的基数
     * @return 当 key 存在且是有序集类型时，返回有序集的基数；当 key 不存在时，返回 0
     */
    public Long zcard(String zkey) {
        return jedis.zcard(zkey);
    }

    /**
     * Sorted set操作（有序集）
     * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员
     * @return score 值在 min 和 max 之间的成员的数量
     */
    public Long zcount(String zkey, double min, double max){
        return jedis.zcount(zkey, min, max);
    }

    /**
     * Sorted set操作（有序集）
     * 返回有序集 key 中，成员 member 的 score 值
     * @return member 成员的 score 值
     */
    public Double zscore(String zkey, String member){
        return jedis.zscore(zkey, member);
    }

    /**
     * Sorted set操作（有序集）
     * 为有序集 key 的成员 member 的 score 值加上增量 increment
     * @return member 成员的新 score 值
     */
    public Double zincrby(String zkey, double increment, String member){
        return jedis.zincrby(zkey, increment, member);
    }

    /**
     * Sorted set操作（有序集）
     * 返回有序集 key 中，指定区间内的成员
     * 其中成员的位置按 score 值递增(从小到大)来排序
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
     * @return 指定区间内，有序集成员的列表
     */
    public Set<String> zrange(String zkey, int start, int end){
        return jedis.zrange(zkey, start, end);
    }

    /**
     * Sorted set操作（有序集）
     * 返回有序集 key 中，指定区间内的成员
     * 其中成员的位置按 score 值递减(从大到小)来排序
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
     * @return 指定区间内，有序集成员的列表
     */
    public Set<String> zrevrange(String zkey, int start, int end){
        return jedis.zrevrange(zkey, start, end);
    }

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
     */
    public Set<String> zrangeByScore(String zkey, double min, double max){
        return jedis.zrangeByScore(zkey, min, max);
    }

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
     * 有序集成员按 score 值递增(从小到大)次序排列。
     * @param zkey key
     * @param min 在不知道最小值时，可以用 -inf代替最小值
     * @param max 在不知道最大值时，可以用 +inf代替最大值
     */
    public Set<String> zrangeByScore(String zkey, String min, String max){
        return jedis.zrangeByScore(zkey, min, max);
    }

    /**
     * Sorted set操作（有序集）
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
     * @return 如果 member 是有序集 key 的成员，返回 member 的排名
     */
    public Long zrank(String zkey, String member){
        return jedis.zrank(zkey, member);
    }

    /**
     * Sorted set操作（有序集）
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序
     * @return 如果 member 是有序集 key 的成员，返回 member 的排名
     */
    public Long zrevrank(String zkey, String member){
        return jedis.zrevrank(zkey, member);
    }

    /**
     * Sorted set操作（有序集）
     * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
     * @return 被移除成员的数量
     */
    public Long zremrangeByScore(String zkey, double start, double end){
        return jedis.zremrangeByScore(zkey, start, end);
    }

    /**
     * Sorted set操作（有序集）
     * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
     * @param zkey key
     * @param start 在不知道最小值时，可以用 -inf代替最小值
     * @param end 在不知道最大值时，可以用 +inf代替最大值
     */
    public Long zremrangeByScore(String zkey, String start, String end){
        return jedis.zremrangeByScore(zkey, start, end);
    }

    /**
     * 事务操作
     * 事务块内的多条命令会按照先后顺序被放进一个队列当中，最后由 EXEC 命令原子性(atomic)地执行。
     * @param block
     * @return
     */
    public List<Object> multiResult(TransactionBlock block){
        return jedis.multi(block);
    }

    /**
     * 事务操作
     * 标记一个事务块的开始
     * @return Transaction对象，可基于Transaction操作
     */
    public Transaction multi(){
        return jedis.multi();
    }

    public Long publish(String channel, String message){
        return jedis.publish(channel, message);
    }

    public void subscribe(JedisPubSub pubSub, String... channels){
        jedis.subscribe(pubSub, channels);
    }

    /**
     * 从redis中获取对象值
     * @param key 二进制key
     */
    public byte[] get(byte[] key){
        return jedis.get(key);
    }

    /**
     * 从redis中根据key删除对象值
     */
    public long del(byte[] key){
        return jedis.del(key);
    }
}
