singleServerConfig:
    idleConnectionTimeout: 10000
    pingTimeout: 3000
    connectTimeout: 10000
    timeout: 3000
    retryAttempts: 3
    retryInterval: 1500
    reconnectionTimeout: 3000
    failedAttempts: 3
    password: SpreDis+dsfjiaDad23SD
    subscriptionsPerConnection: 5
    clientName: null
    address: "redis://192.168.1.73:6379"
    subscriptionConnectionMinimumIdleSize: 1
    subscriptionConnectionPoolSize: 50
    connectionMinimumIdleSize: 2
    connectionPoolSize: 64
    database: 2
threads: 0
nettyThreads: 0
codec: !<org.redisson.codec.FstCodec> {}
transportMode: "NIO"