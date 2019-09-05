    The assignment was to write an app,(well it looks like an utility IMHO)
The idea behind it that a client X had a csv file with some data in it. He needed to parse that data into a suitable DB.
Filtering incomplete rows to a separate file(supposedly for future processing), also logging number of entries.

    This is my SECOND Java application so I used everything I could know.
I understand that it could be done in a different way but jdbc was used. Because CSV file might be extremely large I've decided
to process it in one loop at once writing bad-data csv in one file and sending good entries for future processing.
    Because bad-data csv was required, I thought that it might be used somewhere else for future correction, leading me
to write empty fields with null values. In this manner I've killed 2 stones with 1 bird :D.
Firstly it gave me easier times to filter them just by the fact that they have empty fields and secondly
it will be easier to work with bad-data.csv in the future if it will be required by others, also header of csv was intentionally written.
    After some inspection to the original CSV I saw that there are no double quotes in the text fields making simpler
the algorithm, also it was required to double quote the data that has commas. My thoughts that DB table should be done
with specific data, but for the sake of simplicity I treated all columns just as a text with different max values.
    When dealing with high amount of data is better to optimise all processes so I decided to batch all records.
After preparing statements with good rows, batch is updated in 1 call after that jdbc takes some time to write them.
The result is saved in an array giving the opportunity to see how many row was actually written to DB.
    Most time was spend to make a bearable algorithm so it creates right "rows", it could be done as class instances
with 10 fields in it, if it was required to feed them for other projects like Hibernate to have less dependency.
This utility was done in less than 24 hours so I've tried to do my best.
    After execution the application creates 2 files: bad-data-<timestamp>.csv and the log.txt file, also it writes all good rows to MySQL DB.
Also in the main folder I've put some screenshots after the execution.

Thank you for your attention! :)