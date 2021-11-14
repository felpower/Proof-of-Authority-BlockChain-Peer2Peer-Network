BA05 - Implement private blockchain system

Some several frameworks and systems implement blockchain, e.g., Bitcoin, Ethereum, Multichain, and others. However, to grasp the technology and
understand the main building blocks of blockchain technology, this project aims at implementing a blockchain system. You are expected to define the
data model of the blocks, use the hashing algorithms, implement a function for creating the Merkle Tree, implement a proof-of-authority (PoA)
algorithm, mine new blocks, execute transactions, and a simplified peer-to-peer (P2P) protocol.

Some basic concepts of the Blockchain technology It should be

- Tamper-Proof: Data as part of a block is always tamper-proof. It should be referenced by a cryptographic digest, a hash.
- Decentralized: The blockchain itself is decentralized across the network. There is no master node and every Node has the same copy.
- Transparent: Every node in the network validates and adds a new main.BlockChain.Block to its chain through consensus with other nodes. That means that every
  single node in the network has complete visibility of the data.

A block:

- A block is represented by a hash value. Generating a block(The has Function) is called mining
- Mining a block is computationally expensive(it serves as the “proof of work”)

The hash of a block typically consists of the following data:

- Primarily, the hash of a block consists of the transactions it encapsulates
- The hash also consists of the timestamp of the block's creation
- It also includes a nonce, an arbitrary number used in cryptography
- Finally, the hash of the current block also includes the hash of the previous block
- Multiple nodes in the network can compete to mine the block at the same time. Apart from generating the hash, nodes also have to verify that the
  transactions being added in the block are legitimate.
- The first to mine a block wins!

Verifying and adding a main.BlockChain.Block into the Blockchain.

- For verifying that a block is legitimate all nodes in the network participate in verifying a newly mined block.
- After verifying the newly mined block is added into the blockchain on the consensus of the nodes.

There are several consensus protocols available which we can use for verification.

- The nodes in the network use the same protocol to detect malicious branch of the chain.
- A malicious branch even if introduced will soon be rejected by the majority of the nodes.

