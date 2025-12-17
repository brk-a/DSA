# linked lists

### WTF is a linked list?
* a linear data structure where elements (aka nodes) are stored in memory
* the i<sup>th</sup> node points to the i<sup>th</sup> plus one node for 1 &le; i &lt; n-1
    * where n = len(linked_list)
* node zero (head) is the entry point
* node n-1 (tail) points to `null`
* linked lists allow dynamic memory allocation
* insertions and deletions are more efficient than arrays

* illustration of a singly-linked list

    ```mermaid
        graph LR
            A[0<br/>data: 10<br/>next: → 1] --> B[1<br/>data: 20<br/>next: → 2]
            B --> C[2<br/>data: 30<br/>next: → null]
            C --> D[(null)]
            
            style A fill:#e1f5fe
            style B fill:#e1f5fe
            style C fill:#e1f5fe
            style D fill:#ffebee
    ```

* illustration of a doubly-linked list

    ```mermaid
        graph LR
            Null1[(null)]
            A[0<br/>data: 10<br/>prev: null<br/>next: → 1] 
            B[1<br/>data: 20<br/>prev: ← 0<br/>next: → 2]
            C[2<br/>data: 30<br/>prev: ← 1<br/>next: null]
            
            A -.->|prev| Null1
            Null1 -.->|next| A
            A --> B
            B -.->|prev| A
            B --> C
            C -.->|prev| B
            C --> Null2[(null)]
            Null2 -.->|prev| C
            
            style A fill:#e1f5fe
            style B fill:#e1f5fe
            style C fill:#e1f5fe
            style Null1 fill:#ffebee
            style Null2 fill:#ffebee
    ```


* illustration of a circular linked list

    ```mermaid
        graph LR
            A[0<br/>data: 10<br/>next: → 1] --> B[1<br/>data: 20<br/>next: → 2]
            B --> C[2<br/>data: 30<br/>next: → 0]
            C --> A
            
            classDef circular fill:#fff3e0,stroke:#ff9800,stroke-width:3px
            class A,B,C circular
            
            subgraph cycle ["🔄 Circular Loop"]
                A --- B --- C --- A
            end
    ```

