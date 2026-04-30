package org.activity.cli;
/*
As it can be seen in the help screen, the -h / --help option will also display the help
screen. Let’s take a look at the other options


-a / --add option will add students to the database. The student information will
be delivered to the application in a XML file (you can check the example file provided in
the annex). If an error occurs when importing, the user will be notified (for example,
duplicated ID card, or incorrect phone number format). No students will be imported if
an error occurs.

The -e / --enroll option will enroll an existing student (idCard must be provided)
in an existing course. The student can only be enrolled in one course in a specific
academic year. The first year the student enrolls in a course, the system must register
every subject in the first year as enrolled. For example, if the student is enrolling in DAW,
five modules should be added to the table scores

The following years the student may enroll the second-year subjects and every subject
not passed in the first year. This process will be repeated until the student passes all the
subjects. A subject is passed when the score is equal or superior to five. Once a student
has finished a course, it can’t enroll in the same course again.

To simplify the enrollment process, you must create a stored function returning the
subjects still not passed by a student in a specified course (including first and second
year subjects). The easiest way to develop this function is starting with the opposite
function (subjects passed in a specified course).

Both stored functions are MANDATORY, and must include in the function name YOUR
INITIALS and the current academic year. That is, the subjects_passed function would
be name like: subjects_passed_jrgs_2526. The functions will be delivered in a text
file included in your project (stored_functions.txt). Not including this file will imply
a zero-score in the corresponding qualification item.

The -q / --qualify option will permit the user to introduce (by typing) the scores
obtained by a student in a course. The application will prompt the user for every subject
in the scores table that hasn't been qualified yet (that is, it has a NULL score value). Only
integer values between 0 and 10 will be allowed. The 99 value will permit the user to not
assess a subject.

Finally, the -p / --print option will show the results of a student. For example, if we
call the application with the following parameter
* */

public enum Command {
    HELP("--h","--help"),
    ADD("--a","--add"),
    ENROLL("--e","--enroll"),
    QUALIFY("--q","--qualify"),
    PRINT("--p","--print"),
    UNKNOWN("","");

    private final String shortOpt;
    private final String longOpt;

    Command(String shortOpt, String longOpt) {
        this.shortOpt = shortOpt;
        this.longOpt = longOpt;
    }

    public static Command from(String arg) {
        for (Command c : values()) {
            if (c.shortOpt.equals(arg) || c.longOpt.equals(arg)) {
                return c;
            }
        }
        return UNKNOWN;
    }
}
