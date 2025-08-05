package com.peknight.os.signal

import com.peknight.os.signal.SignalAction.*
import com.peknight.os.signal.SignalStandard.*

enum Signal(val x86Arm: Option[Int], val alpha: Option[Int], val sparc: Option[Int], val mips: Option[Int],
            val parisc: Option[Int], val standard: Option[SignalStandard], val action: Option[SignalAction],
            val comment: String):
  case SIGHUP extends Signal(Some(1), Some(1), Some(1), Some(1), Some(1), Some(P1990), Some(Term),
    "Hangup detected on controlling terminal or death of controlling process")
  case SIGINT extends Signal(Some(2), Some(2), Some(2), Some(2), Some(2), Some(P1990), Some(Term),
    "Interrupt from keyboard")
  case SIGQUIT extends Signal(Some(3), Some(3), Some(3), Some(3), Some(3), Some(P1990), Some(Core),
    "Quit from keyboard")
  case SIGILL extends Signal(Some(4), Some(4), Some(4), Some(4), Some(4), Some(P1990), Some(Core),
    "Illegal Instruction")
  case SIGTRAP extends Signal(Some(5), Some(5), Some(5), Some(5), Some(5), Some(P2001), Some(Core),
    "Trace/breakpoint trap")
  case SIGABRT extends Signal(Some(6), Some(6), Some(6), Some(6), Some(6), Some(P1990), Some(Core),
    "Abort signal from abort(3)")
  case SIGIOT extends Signal(Some(6), Some(6), Some(6), Some(6), Some(6), None, Some(Core),
    "IOT trap. A synonym for SIGABRT")
  case SIGBUS extends Signal(Some(7), Some(10), Some(10), Some(10), Some(10), Some(P2001), Some(Core),
    "Bus error (bad memory access)")
  case SIGEMT extends Signal(None, Some(7), Some(7), Some(7), None, None, Some(Term), "Emulator trap")
  case SIGFPE extends Signal(Some(8), Some(8), Some(8), Some(8), Some(8), Some(P1990), Some(Core),
    "Erroneous arithmetic operation")
  case SIGKILL extends Signal(Some(9), Some(9), Some(9), Some(9), Some(9), Some(P1990), Some(Term), "Kill signal")
  case SIGUSR1 extends Signal(Some(10), Some(30), Some(30), Some(16), Some(16), Some(P1990), Some(Term),
    "User-defined signal 1")
  case SIGSEGV extends Signal(Some(11), Some(11), Some(11), Some(11), Some(11), Some(P1990), Some(Core),
    "Invalid memory reference")
  case SIGUSR2 extends Signal(Some(12), Some(31), Some(31), Some(17), Some(17), Some(P1990), Some(Term),
    "User-defined signal 2")
  case SIGPIPE extends Signal(Some(13), Some(13), Some(13), Some(13), Some(13), Some(P1990), Some(Term),
    "Broken pipe: write to pipe with no readers; see pipe(7)")
  case SIGALRM extends Signal(Some(14), Some(14), Some(14), Some(14), Some(14), Some(P1990), Some(Term),
    "Timer signal from alarm(2)")
  case SIGTERM extends Signal(Some(15), Some(15), Some(15), Some(15), Some(15), Some(P1990), Some(Term),
    "Termination signal")
  case SIGSTKFLT extends Signal(Some(16), None, None, None, Some(7), None, Some(Term),
    "Stack fault on coprocessor (unused)")
  case SIGCHLD extends Signal(Some(17), Some(20), Some(20), Some(18), Some(18), Some(P1990), Some(Ign),
    "Child stopped or terminated")
  case SIGCLD extends Signal(None, None, None, Some(18), None, None, Some(Ign), "A synonym for SIGCHLD")
  case SIGCONT extends Signal(Some(18), Some(19), Some(19), Some(25), Some(26), Some(P1990), Some(Cont),
    "Continue if stopped")
  case SIGSTOP extends Signal(Some(19), Some(17), Some(17), Some(23), Some(24), Some(P1990), Some(Stop),
    "Stop process")
  case SIGTSTP extends Signal(Some(20), Some(18), Some(18), Some(24), Some(25), Some(P1990), Some(Stop),
    "Stop typed at terminal")
  case SIGTTIN extends Signal(Some(21), Some(21), Some(21), Some(26), Some(27), Some(P1990), Some(Stop),
    "Terminal input for background process")
  case SIGTTOU extends Signal(Some(22), Some(22), Some(22),  Some(27), Some(28), Some(P1990), Some(Stop),
    "Terminal output for background process")
  case SIGURG extends Signal(Some(23), Some(16), Some(16), Some(21), Some(29), Some(P2001), Some(Ign),
    "Urgent condition on socket (4.2BSD)")
  case SIGXCPU extends Signal(Some(24), Some(24), Some(24), Some(30), Some(12), Some(P2001), Some(Core),
    "CPU time limit exceeded (4.2BSD); see setrlimit(2)")
  case SIGXFSZ extends Signal(Some(25), Some(25), Some(25), Some(31), Some(30), Some(P2001), Some(Core),
    "File size limit exceeded (4.2BSD); see setrlimit(2)")
  case SIGVTALRM extends Signal(Some(26), Some(26), Some(26), Some(28), Some(20), Some(P2001), Some(Term),
    "Virtual alarm clock (4.2BSD)")
  case SIGPROF extends Signal(Some(27), Some(27), Some(27), Some(29), Some(21), Some(P2001), Some(Term),
    "Profiling timer expired")
  case SIGWINCH extends Signal(Some(28), Some(28), Some(28), Some(20), Some(23), None, Some(Ign),
    "Window resize signal (4.3BSD, Sun)")
  case SIGIO extends Signal(Some(29), Some(23), Some(23), Some(22), Some(22), None, Some(Term),
    "I/O now possible (4.2BSD)")
  case SIGPOLL extends Signal(Some(29), Some(23), Some(23), Some(22), Some(22), Some(P2001), Some(Term),
    "Pollable event (Sys V); synonym for SIGIO")
  case SIGPWR extends Signal(Some(30), Some(29), None, Some(19), Some(19), None, Some(Term),
    "Power failure (System V)")
  case SIGINFO extends Signal(None, Some(29), None, None, None, None, None, "A synonym for SIGPWR")
  case SIGLOST extends Signal(None, None, Some(29), None, None, None, Some(Term), "File lock lost (unused)")
  case SIGSYS extends Signal(Some(31), Some(12), Some(12), Some(12), Some(31), Some(P2001), Some(Core),
    "Bad system call (SVr4); see also seccomp(2)")
  case SIGUNUSED extends Signal(Some(31), None, None, None, Some(31), None, Some(Core), "Synonymous with SIGSYS")
end Signal
