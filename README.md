# Tomasulo Simulator

A Java console simulator for Tomasulo's algorithm, built as an educational computer architecture project.

The program models a simplified floating-point processor with:

- floating-point registers `F1` through `F16`
- add/sub reservation stations
- mul/div reservation stations
- load buffers
- store buffers
- a small hard-coded data memory
- an instruction queue read from standard input

It advances the simulation cycle by cycle and prints the internal machine state after each cycle.

When more than one unit is ready to write back, the simulator now uses instruction issue order to choose the oldest ready result first.

## Supported Instructions

The input parser recognizes these instructions:

```text
L.D destinationRegister memoryAddress
S.D sourceRegister memoryAddress
ADD.D destinationRegister sourceRegister1 sourceRegister2
SUB.D destinationRegister sourceRegister1 sourceRegister2
MUL.D destinationRegister sourceRegister1 sourceRegister2
DIV.D destinationRegister sourceRegister1 sourceRegister2
```

Example:

```text
L.D F1 0
L.D F2 1
ADD.D F3 F1 F2
```

## Project Structure

- `src/Main.java`: program entry point, input parsing, simulation loop, issue/execute/write-back logic, and state printing
- `src/Tomasulo.java`: main simulator state container
- `src/Register.java`: floating-point register model
- `src/ReservationSlot.java`: add/sub and mul/div reservation station entry
- `src/LoadBufferSlot.java`: load buffer entry
- `src/StoreBufferSlot.java`: store buffer entry

## Requirements

- JDK 8 or newer
- A terminal that can provide standard input

No external dependencies are required.

## Compile

```bash
javac src/*.java
```

## Run

```bash
java -cp src Main
```

The program prompts for:

1. add/sub reservation station size
2. mul/div reservation station size
3. load latency
4. store latency
5. add latency
6. sub latency
7. mul latency
8. div latency
9. instruction lines until EOF

On macOS/Linux, press `Ctrl+D` after entering the instruction list.

## Input Validation

The simulator validates:

- reservation station sizes: positive integers
- latencies: non-negative integers
- registers: `F1` through `F16`
- memory addresses: `0` through `100`
- instruction formats and supported operation names

Invalid instruction lines are skipped with a message, and valid instructions continue to run.

## Sample Input

```text
2
2
2
2
2
2
2
2
L.D F1 0
L.D F2 1
ADD.D F3 F1 F2
```

You can pipe the sample into the simulator:

```bash
printf '2\n2\n2\n2\n2\n2\n2\n2\nL.D F1 0\nL.D F2 1\nADD.D F3 F1 F2\n' | java -cp src Main
```

## Example Output

This smaller input terminates successfully:

```bash
printf '2\n2\n1\n1\n1\n1\n1\n1\nL.D F1 0\n' | java -cp src Main
```

The simulator prints compact tables after every cycle. A shortened excerpt from the final cycle looks like this:

```text
Enter addSub Reservation station size:
Enter mulDiv Reservation station size:
Enter load latency:
Enter store latency:
Enter add latency:
Enter sub latency:
Enter mul latency:
Enter div latency:
///////////////////Write code///////////////////

=== Cycle 3 ===
Load Buffers
Tag  Busy  Addr    Left  Ord
L0   false 0       0     -1
L1   false 0       0     -1
L2   false 0       0     -1

Registers
F1=10  F2=  F3=  F4=  F5=  F6=  F7=  F8=  F9=  F10=  F11=  F12=  F13=  F14=  F15=  F16=

Data Memory
Mem[0]=10  Mem[1]=11  Mem[2]=5  Mem[3]=6

Finished
```

In this run, `L.D F1 0` loads the value at memory address `0`. Since `Mem[0]` is initialized to `10`, the final register file shows `F1` with value `10`.

## Initial Data Memory

The simulator initializes memory in `src/Main.java`:

- `Mem[0] = 10`
- `Mem[1] = 11`
- `Mem[2] = 5`
- `Mem[3] = 6`

All other entries in the `dataMemory` array start as `null`.

## Test Status

Checked on May 8, 2026.

Compile test:

```bash
javac src/*.java
```

Result: passed.

Runtime smoke test 1:

```bash
printf '2\n2\n1\n1\n1\n1\n1\n1\nL.D F1 0\n' | java -cp src Main
```

Result: passed. The simulator finishes and writes `10` from `Mem[0]` into `F1`.

Runtime smoke test 2:

```bash
printf '2\n2\n2\n2\n2\n2\n2\n2\nL.D F1 0\nL.D F2 1\nADD.D F3 F1 F2\n' | java -cp src Main
```

Result: passed. The simulator finishes and writes:

```text
F1 = 10
F2 = 11
F3 = 21.0
```

Runtime smoke test 3:

```bash
printf '2\n2\n1\n1\n1\n1\n1\n1\nL.D F1 0\nL.D F2 2\nSUB.D F3 F1 F2\nMUL.D F4 F1 F2\nDIV.D F5 F1 F2\nS.D F3 4\n' | java -cp src Main
```

Result: passed. The simulator finishes and writes:

```text
F1 = 10
F2 = 5
F3 = 5.0
F4 = 50.0
F5 = 2.0
Mem[4] = 5.0
```

Runtime validation test:

```bash
printf '2\n2\n1\n1\n1\n1\n1\n1\nBAD F1 0\nL.D F17 0\nL.D F1 101\nL.D F1 0\n' | java -cp src Main
```

Result: passed. The simulator skips the invalid lines, runs the valid `L.D F1 0`, and finishes with `F1 = 10`.

## Known Issues

- There are no automated tests.
- Most simulation logic is concentrated in `src/Main.java`.
- The simulator is still a simplified educational model, not a complete cycle-accurate CPU implementation.

## Suggested Next Improvements

- Split issue, execute, and write-back behavior into smaller testable methods.
- Add automated tests for load, store, arithmetic, and dependency scenarios.
- Add configurable memory initialization instead of using only hard-coded memory values.
