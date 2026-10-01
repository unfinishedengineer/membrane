
```markdown
JVM A                                      JVM B
Producer                                   Consumer
   │                                          │
   │ MappedByteBuffer                         │ MappedByteBuffer
   ▼                                          ▼
┌──────────────────────────────────────────────────────┐
│                 shared-memory.dat                    │
│                                                      │
│  0..7        sequence                                │
│  8..11       message length                          │
│  12..N       message bytes                           │
└──────────────────────────────────────────────────────┘