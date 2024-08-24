from cachetools import LRUCache


class LRUCacheWithCallback(LRUCache):
    def __init__(self, callback, max_size=1000):
        super().__init__(maxsize=max_size)
        self.callback = callback

    def __setitem__(self, key, value):
        """Enhanced setitem method to evict the oldest item when the cache is full."""
        if len(self) >= self.maxsize:
            old_key, old_value = next(iter(self.items()))
            super().__delitem__(old_key)
            if self.callback:
                self.callback(old_key, old_value)

        super().__setitem__(key, value)


class CacheManager:
    def __init__(self, callback=None, max_size=1000):
        self.cache = LRUCacheWithCallback(callback=callback, max_size=max_size)

    def set_cache(self, uid, context_id, value):
        self.cache[(uid, context_id)] = value

    def get_cache(self, uid, context_id):
        return self.cache.get((uid, context_id))

    def delete_cache(self, uid, context_id):
        del self.cache[(uid, context_id)]

    def clear_cache(self):
        self.cache.clear()


if __name__ == "__main__":

    def on_evict_callback(key, value):
        print(f"Evicted key: {key}, value: {value}")

    cache_manager = CacheManager(callback=on_evict_callback, max_size=2)

    cache_manager.set_cache("user1", "context1", {"messages": ["Hello"]})
    cache_manager.set_cache("user1", "context1", {"messages": ["Hello"]})
    cache_manager.set_cache("user1", "context1", {"messages": ["Hello"]})
    cache_manager.set_cache("user2", "context2", {"messages": ["Hi"]})

    cache_manager.set_cache("user3", "context3", {"messages": ["How are you?"]})

    result = cache_manager.get_cache("user1", "context1")
    # Current cache is full, so the oldest item (user2, context2) is evicted
    print(result)
