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
- `src/RegFileEntry.java`: unused register-file entry placeholder
- `Parser.java`: unused parser placeholder

## Requirements

- JDK 8 or newer
- A terminal that can provide standard input

No external dependencies are required.

## Compile

```bash
javac src/*.java Parser.java
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
javac src/*.java Parser.java
```

Result: passed.

Runtime smoke test:

```bash
printf '2\n2\n2\n2\n2\n2\n2\n2\nL.D F1 0\nL.D F2 1\nADD.D F3 F1 F2\n' | java -cp src Main
```

Result: the simulator starts, issues instructions, and prints cycle state, but this dependency scenario currently fails with:

```text
java.lang.NumberFormatException: empty String
```

The exception occurs in `Main.WriteBack` while handling the `ADD.D` result. The project should therefore be treated as a partially working Tomasulo simulator prototype rather than a complete implementation.

## Known Issues

- Some dependency scenarios fail during write-back.
- There are no automated tests.
- Most simulation logic is concentrated in `src/Main.java`.
- `Parser.java` and `src/RegFileEntry.java` are currently unused placeholders.
- The simulator prints a very large amount of state for each cycle.

## Suggested Next Improvements

- Split issue, execute, and write-back behavior into smaller testable methods.
- Add automated tests for load, store, arithmetic, and dependency scenarios.
- Fix add/sub write-back to read values from the add/sub reservation station.
- Add a maximum-cycle guard for debugging non-terminating programs.
