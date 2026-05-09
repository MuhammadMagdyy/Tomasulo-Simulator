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

It advances the simulation cycle by cycle and prints a trace of what happened in each cycle.

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

The program reads one value per line from standard input. The first 8 lines configure the machine:

1. add/sub reservation station size
2. mul/div reservation station size
3. load latency
4. store latency
5. add latency
6. sub latency
7. mul latency
8. div latency

Every remaining line is one instruction. On macOS/Linux, press `Ctrl+D` after entering the instruction list.

For example, this input:

```text
2
2
2
2
2
2
4
4
L.D F1 0
L.D F2 1
ADD.D F3 F1 F2
```

means:

- `2`: create 2 add/sub reservation stations.
- `2`: create 2 mul/div reservation stations.
- `2`: loads take 2 cycles.
- `2`: stores take 2 cycles.
- `2`: adds take 2 cycles.
- `2`: subtracts take 2 cycles.
- `4`: multiplies take 4 cycles.
- `4`: divides take 4 cycles.
- `L.D F1 0`: load `Mem[0]` into register `F1`.
- `L.D F2 1`: load `Mem[1]` into register `F2`.
- `ADD.D F3 F1 F2`: add `F1 + F2` and write the result to `F3`.

The simulator accepts blank lines in the instruction section and ignores them.

## Input Validation

The simulator validates:

- reservation station sizes: positive integers
- latencies: non-negative integers
- registers: `F1` through `F16`
- memory addresses: `0` through `100`
- instruction formats and supported operation names
- `L.D` reads only initialized memory addresses
- arithmetic instructions read only registers that already have a value or are produced by an earlier accepted instruction
- `S.D` stores only registers that already have a value or are produced by an earlier accepted instruction

Invalid instruction lines are skipped with a message, and valid instructions continue to run.

The simulator also prevents a load from passing an older pending store to the same memory address.

At runtime, division by zero is reported in the trace and the destination receives `NaN` instead of crashing the simulator.

## Simple Sample Input

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

This sample uses the hard-coded initial memory values:

- `Mem[0] = 10`
- `Mem[1] = 11`

Expected final values:

```text
F1 = 10
F2 = 11
F3 = 21.0
```

## More Complicated Examples

### Example 1: Chained Dependencies, Mixed Units, and Store/Load Ordering

```bash
printf '3\n2\n2\n2\n2\n2\n4\n4\nL.D F1 0\nL.D F2 1\nL.D F3 2\nL.D F4 3\nADD.D F5 F1 F2\nSUB.D F6 F2 F3\nMUL.D F7 F5 F6\nDIV.D F8 F7 F4\nS.D F8 8\nL.D F9 8\nADD.D F10 F9 F1\n' | java -cp src Main
```

Input explanation:

- `3`: create 3 add/sub reservation stations, enough to hold several waiting add/sub instructions.
- `2`: create 2 mul/div reservation stations.
- `2, 2, 2, 2, 4, 4`: load/store/add/sub take 2 cycles; mul/div take 4 cycles.
- `L.D F1 0`, `L.D F2 1`, `L.D F3 2`, `L.D F4 3`: load the initialized values `10`, `11`, `5`, and `6`.
- `ADD.D F5 F1 F2`: computes `10 + 11 = 21`.
- `SUB.D F6 F2 F3`: computes `11 - 5 = 6`.
- `MUL.D F7 F5 F6`: waits for both `F5` and `F6`, then computes `21 * 6 = 126`.
- `DIV.D F8 F7 F4`: waits for `F7`, then computes `126 / 6 = 21`.
- `S.D F8 8`: stores the computed value into `Mem[8]`.
- `L.D F9 8`: loads from `Mem[8]`, but waits until the older store to the same address commits.
- `ADD.D F10 F9 F1`: waits for `F9`, then computes `21 + 10 = 31`.

Expected final values:

```text
F1 = 10
F2 = 11
F3 = 5
F4 = 6
F5 = 21.0
F6 = 6.0
F7 = 126.0
F8 = 21.0
F9 = 21.0
F10 = 31.0
Mem[8] = 21.0
```

This example is useful because it shows several Tomasulo behaviors in one run:

- consumers wait on producer tags such as `A0`, `M0`, or `L0`;
- independent units can make progress in the same cycle;
- only one ready result is selected for write-back in a cycle;
- a load cannot pass an older pending store to the same memory address.

### Example 2: Store a Derived Value and Load It Later

```bash
printf '2\n2\n1\n2\n1\n1\n2\n2\nL.D F1 0\nL.D F2 2\nSUB.D F3 F1 F2\nS.D F3 6\nL.D F4 6\nMUL.D F5 F4 F2\n' | java -cp src Main
```

Input explanation:

- `2, 2`: create 2 stations for add/sub and 2 stations for mul/div.
- `1, 2, 1, 1, 2, 2`: loads take 1 cycle, stores take 2 cycles, add/sub take 1 cycle, mul/div take 2 cycles.
- `L.D F1 0`: loads `10` from `Mem[0]`.
- `L.D F2 2`: loads `5` from `Mem[2]`.
- `SUB.D F3 F1 F2`: computes `10 - 5 = 5`.
- `S.D F3 6`: stores `5.0` into `Mem[6]`.
- `L.D F4 6`: reads the value that was just stored.
- `MUL.D F5 F4 F2`: computes `5.0 * 5 = 25`.

Expected final values:

```text
F1 = 10
F2 = 5
F3 = 5.0
F4 = 5.0
F5 = 25.0
Mem[6] = 5.0
```

### Example 3: Invalid Lines Are Skipped

```bash
printf '2\n2\n1\n1\n1\n1\n1\n1\nL.D F17 0\nL.D F1 50\nL.D F1 0\nADD.D F2 F1 F3\nL.D F3 1\nADD.D F4 F1 F3\n' | java -cp src Main
```

Input explanation:

- `L.D F17 0` is skipped because valid registers are only `F1` through `F16`.
- `L.D F1 50` is skipped because `Mem[50]` is uninitialized.
- `L.D F1 0` is accepted and loads `10`.
- `ADD.D F2 F1 F3` is skipped because `F3` has no value or earlier producer at that point.
- `L.D F3 1` is accepted and loads `11`.
- `ADD.D F4 F1 F3` is accepted and computes `10 + 11 = 21`.

Expected final values:

```text
F1 = 10
F3 = 11
F4 = 21.0
```

## Example Output

This smaller input terminates successfully:

```bash
printf '2\n2\n1\n1\n1\n1\n1\n1\nL.D F1 0\n' | java -cp src Main
```

The simulator prints a per-cycle trace. A shortened excerpt looks like this:

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

Cycle 1
  ISSUE: L.D F1 0 -> L0
  Registers: F1=L0  F2=  F3=  F4= ...
  Memory: Mem[0]=10  Mem[1]=11  Mem[2]=5  Mem[3]=6

Cycle 2
  EXECUTE: L0 load Mem[0] (0 cycles left)
  WRITE-BACK: L0 broadcasts 10
  Registers: F1=10  F2=  F3=  F4= ...
  Memory: Mem[0]=10  Mem[1]=11  Mem[2]=5  Mem[3]=6

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

Checked on May 9, 2026.

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

Runtime logical-validation test:

```bash
printf '2\n2\n1\n1\n1\n1\n1\n1\nL.D F1 10\nADD.D F2 F3 F4\nL.D F1 0\n' | java -cp src Main
```

Result: passed. The simulator rejects the uninitialized memory load and the arithmetic instruction that reads uninitialized registers, then runs the valid load.

Runtime store/load ordering test:

```bash
printf '2\n2\n1\n2\n1\n1\n1\n1\nL.D F1 0\nS.D F1 5\nL.D F2 5\n' | java -cp src Main
```

Result: passed. The trace shows `L.D F2 5` waiting until the older `S.D F1 5` commits, then loading the stored value.

Runtime complicated example test:

```bash
printf '3\n2\n2\n2\n2\n2\n4\n4\nL.D F1 0\nL.D F2 1\nL.D F3 2\nL.D F4 3\nADD.D F5 F1 F2\nSUB.D F6 F2 F3\nMUL.D F7 F5 F6\nDIV.D F8 F7 F4\nS.D F8 8\nL.D F9 8\nADD.D F10 F9 F1\n' | java -cp src Main
```

Result: passed. The simulator finishes with `F10 = 31.0` and `Mem[8] = 21.0`.

Runtime invalid-line example test:

```bash
printf '2\n2\n1\n1\n1\n1\n1\n1\nL.D F17 0\nL.D F1 50\nL.D F1 0\nADD.D F2 F1 F3\nL.D F3 1\nADD.D F4 F1 F3\n' | java -cp src Main
```

Result: passed. The simulator skips the invalid lines and finishes with `F4 = 21.0`.

## Known Issues

- There are no automated tests.
- Most simulation logic is concentrated in `src/Main.java`.
- The simulator is still a simplified educational model, not a complete cycle-accurate CPU implementation.

## Suggested Next Improvements

- Split issue, execute, and write-back behavior into smaller testable methods.
- Add automated tests for load, store, arithmetic, and dependency scenarios.
- Add configurable memory initialization instead of using only hard-coded memory values.
