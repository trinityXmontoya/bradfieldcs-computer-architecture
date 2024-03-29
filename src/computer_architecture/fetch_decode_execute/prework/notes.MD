**1.** A diagram you have drawn of the main components of a computer, and how they are connected; and

![main computer components](../../../../resources/computer_architecture/fetch_decode_execute/prework/main-computer-components.JPG)


**2.** A paragraph or two of prose describing your understanding of the fetch-decode-execute cycle, and how the relevant components of the computer are involved in each step.

The fetch-decode-execute cycle is how a computer executes a program.

First, you need to have the instructions for that program and any necessary data in the computer's memory (these can come from an external storage device like a CD from a game company, or internal storage like an application you previously downloaded to the computer). The control unit of the CPU handles the orchestration of these instructions and data, executing the program instructions step-by-step. It has an internal Program Counter so it knows the memory address of the next instruction to run.

The control unit gets the first instruction's address from the Program Counter, this address is now in the Memory Address Register (MAR). The control unit _fetches_ the instruction from the address in the current MAR. These instructions are temporarily held in the Current Instruction Register (CIR). The control unit then _decodes_ the instructions to determine what actions need to be performed and where to find the necessary data to use as input for those actions. The decoded instructions tell it the address of every piece of data it will need. It queues up these addresses on the 'address bus'. The control unit then directs the data from the 'address bus' to the 'accumulator register' in the CPU via the 'data bus'. This data is now accessible by the arithmetic/logic unit (ALU). The ALU _executes_ the instructions on the provided data. The results of each step of the execution are returned to the 'accumulator' register. Once the necessary actions have been performed, the result is temporarily moved to the 'storage register'. Depending on the original instructions, the control unit either sends those results to secondary storage (ex. the hard drive) or an output device (ex. the screen). This cycle continues by the control unit getting the next address from the Program Counter and repeating the process. Eventually one of these instructions will be to 'stop'.
