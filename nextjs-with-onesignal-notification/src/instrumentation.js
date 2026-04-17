function createNoopStorage() {
    const store = new Map();

    return {
        get length() {
            return store.size;
        },
        clear() {
            store.clear();
        },
        getItem(key) {
            return store.has(key) ? store.get(key) : null;
        },
        key(index) {
            const keys = Array.from(store.keys());
            return keys[index] ?? null;
        },
        removeItem(key) {
            store.delete(key);
        },
        setItem(key, value) {
            store.set(key, String(value));
        },
    };
}

export async function register() {
    if (typeof window !== 'undefined') {
        return;
    }

    const hasValidStorage =
        typeof globalThis.localStorage !== 'undefined' &&
        typeof globalThis.localStorage?.getItem === 'function' &&
        typeof globalThis.localStorage?.setItem === 'function';

    if (hasValidStorage) {
        return;
    }

    // Some local Node runtimes expose a broken localStorage object on the server.
    // Patch it so server rendering does not crash while browser storage remains unchanged.
    Object.defineProperty(globalThis, 'localStorage', {
        configurable: true,
        enumerable: false,
        writable: true,
        value: createNoopStorage(),
    });
}
