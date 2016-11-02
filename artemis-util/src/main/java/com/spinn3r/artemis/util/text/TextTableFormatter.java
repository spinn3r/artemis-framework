package com.spinn3r.artemis.util.text;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.Strings;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Computes a format of text with headers and fixed width based on the inputs.
 */
public class TextTableFormatter {

    private static final String LINE_NUMBER_FORMAT = "%,d.";

    protected Table table = new Table();

    private TextTableFormatter() {
    }

    public String format() {

        List<List<String>> lines = Lists.newArrayList();

//        lines.add(withLineNumberPrefix(headings));
//        lines.add(withLineNumberPrefix(underlines));
//        //lines.addAll(withLineNumbers(this.rows));
//        lines.addAll(this.rows);

        return TableFormatter.format(table.withLineNumbers().toCells());

    }

    private ImmutableList<String> withLineNumberPrefix(List<String> data) {

        List<String> result = Lists.newArrayList();
        result.add("");
        result.addAll(data);

        return ImmutableList.copyOf(result);

    }

    private ImmutableList<ImmutableList<String>> withJustification(int col, Justification justification) {

        List<ImmutableList<String>> result = Lists.newArrayList();



        return ImmutableList.copyOf(result);

    }

    protected void headings(String... headings) {

        table.setHeadings(ImmutableList.copyOf(Arrays.asList(headings)));
        table.setUnderlines(createUnderlines(headings));
        table.setJustifications(createJustifications(headings));

    }

    protected ImmutableList<Justification> createJustifications(String... headings) {

        List<Justification> result = Lists.newArrayList();

        for (String heading : headings) {
            result.add(Justification.RIGHT);
        }

        return ImmutableList.copyOf(result);

    }

    protected ImmutableList<String> createUnderlines(String... headings) {

        List<String> result = Lists.newArrayList();

        for (String heading : headings) {
            result.add(Strings.repeat("-", heading.length()));
        }

        return ImmutableList.copyOf(result);

    }

    public static TextTableFormatter1 forHeadings(String col0) {
        TextTableFormatter1 result = new TextTableFormatter1();
        result.headings(col0);
        return result;
    }

    public static TextTableFormatter2 forHeadings(String col0, String col1) {
        TextTableFormatter2 result = new TextTableFormatter2();
        result.headings(col0, col1);
        return result;
    }

    public static TextTableFormatter3 forHeadings(String col0, String col1, String col2) {
        TextTableFormatter3 result = new TextTableFormatter3();
        result.headings(col0, col1, col2);
        return result;
    }

    public static TextTableFormatter4 forHeadings(String col0, String col1, String col2, String col3) {
        TextTableFormatter4 result = new TextTableFormatter4();
        result.headings(col0, col1, col2, col3);
        return result;
    }

    public static TextTableFormatter5 forHeadings(String col0, String col1, String col2, String col3, String col4) {
        TextTableFormatter5 result = new TextTableFormatter5();
        result.headings(col0, col1, col2, col3, col4);
        return result;
    }

    public static TextTableFormatter6 forHeadings(String col0, String col1, String col2, String col3, String col4, String col5) {
        TextTableFormatter6 result = new TextTableFormatter6();
        result.headings(col0, col1, col2, col3, col4, col5);
        return result;
    }

    public static TextTableFormatter7 forHeadings(String col0, String col1, String col2, String col3, String col4, String col5, String col6) {
        TextTableFormatter7 result = new TextTableFormatter7();
        result.headings(col0, col1, col2, col3, col4, col5, col6);
        return result;
    }

    public static class TextTableFormatter1 extends TextTableFormatter {

        public TextTableFormatter1 row(Object col0) {
            table.addRow(ImmutableList.of(col0.toString()));
            return this;
        }

    }

    public static class TextTableFormatter2 extends TextTableFormatter {

        public TextTableFormatter2 row(Object col0, Object col1) {
            table.addRow(ImmutableList.of(col0.toString(), col1.toString()));
            return this;
        }

    }

    public static class TextTableFormatter3 extends TextTableFormatter {

        public TextTableFormatter3 row(Object col0, Object col1, Object col2) {
            table.addRow(ImmutableList.of(col0.toString(), col1.toString(), col2.toString()));
            return this;
        }

    }

    public static class TextTableFormatter4 extends TextTableFormatter {

        public TextTableFormatter4 row(Object col0, Object col1, Object col2, Object col3) {
            table.addRow(ImmutableList.of(col0.toString(), col1.toString(), col2.toString(), col3.toString()));
            return this;
        }

    }

    public static class TextTableFormatter5 extends TextTableFormatter {

        public TextTableFormatter5 row(Object col0, Object col1, Object col2, Object col3, Object col4) {
            table.addRow(ImmutableList.of(col0.toString(), col1.toString(), col2.toString(), col3.toString(), col4.toString()));
            return this;
        }

    }

    public static class TextTableFormatter6 extends TextTableFormatter {

        public TextTableFormatter6 row(Object col0, Object col1, Object col2, Object col3, Object col4, Object col5) {
            table.addRow(ImmutableList.of(col0.toString(), col1.toString(), col2.toString(), col3.toString(), col4.toString(), col5.toString()));
            return this;
        }

    }

    public static class TextTableFormatter7 extends TextTableFormatter {

        public TextTableFormatter7 row(Object col0, Object col1, Object col2, Object col3, Object col4, Object col5, Object col6) {
            table.addRow(ImmutableList.of(col0.toString(), col1.toString(), col2.toString(), col3.toString(), col4.toString(), col5.toString(), col6.toString()));
            return this;
        }

    }

    static class Table {

        protected ImmutableList<String> headings = ImmutableList.of();

        protected ImmutableList<Justification> justifications = ImmutableList.of();

        protected ImmutableList<String> underlines = ImmutableList.of();

        protected List<ImmutableList<String>> rows = Lists.newArrayList();

        public Table() {
        }

        public Table(ImmutableList<ImmutableList<String>> rows) {
            this.rows = rows;
        }

        public void setHeadings(ImmutableList<String> headings) {
            this.headings = headings;
        }

        public void setJustifications(ImmutableList<Justification> justifications) {
            this.justifications = justifications;
        }

        public void setUnderlines(ImmutableList<String> underlines) {
            this.underlines = underlines;
        }

        public void addRow(ImmutableList<String> row) {
            rows.add(row);
        }

        public Table withLineNumbers() {

            Table table = new Table();

            // have to adjust headings, justification, etc.

            table.setHeadings(new StringListBuilder()
                                .add("")
                                .addAll(this.headings).toImmutableList());

            table.setJustifications(new ListBuilder<Justification>()
                                      .add(Justification.LEFT)
                                      .addAll(this.justifications).toImmutableList());

            table.setUnderlines(new StringListBuilder()
                                .add("")
                                .addAll(this.underlines)
                                .toImmutableList());

            for (int i = 0; i < rows.size(); i++) {

                List<String> row = rows.get(i);
                table.addRow(new StringListBuilder()
                               .add(String.format(LINE_NUMBER_FORMAT, i+1))
                               .addAll(row).toImmutableList());

            }

            return table;

        }

        private List<List<String>> justify(List<List<String>> cells) {

            List<List<String>> result = Lists.newArrayList();

            Grid grid = new Grid(cells);

            ImmutableList<Integer> columnWidths = grid.columnWidths();

            for (List<String> row : cells) {
                List<String> newRow = Lists.newArrayList();
                for (int i = 0; i < row.size(); i++) {
                    Justification justification = justifications.get(0);
                    String value = row.get(i);
                    int columnWidth = columnWidths.get(i);
                    newRow.add(Justifier.justify(justification, value, columnWidth));
                }
                result.add(newRow);
            }

            return result;

        }

        public List<List<String>> toCells() {

            List<List<String>> result = Lists.newArrayList();

            // TODO: apply justification...

            result.add(ImmutableList.copyOf(headings));
            result.add(ImmutableList.copyOf(underlines));
            result.addAll(rows);

            return justify(result);
            //return result;

        }

    }

    static class Justifier {

        public static String left(String text, int width) {
            String fmt = "%-" + width + "s";
            return String.format(fmt, text);
        }

        public static String right(String text, int width) {
            String fmt = "%" + width + "s";
            return String.format(fmt, text);
        }

        public static String justify(Justification justification, String text, int width) {

            if (justification.equals(Justification.LEFT)) {
                return left(text, width);
            } else {
                return right(text, width);
            }

        }

    }

    static class StringListBuilder extends ListBuilder<String> {

    }

    static class ListBuilder<T> {

        private List<T> list = Lists.newArrayList();

        public ListBuilder<T> add(T entry) {
            list.add(entry);
            return this;
        }

        public ListBuilder<T> addAll(Collection<T> entry) {
            list.addAll(entry);
            return this;
        }

        public ImmutableList<T> toImmutableList() {
            return ImmutableList.copyOf(list);
        }

        public List<T> toList() {
            return Lists.newArrayList(list);
        }

    }

    /**
     * Functions for an NxN grid of objects..
     */
    static class Grid {

        private final List<List<String>> table;

        public Grid(List<List<String>> table) {
            this.table = table;
        }

        private ImmutableList<Integer> columnWidths() {

            List<Integer> result = Lists.newArrayList();

            List<String> first = table.get( 0 );

            for (int i = 0; i < first.size(); i++) {
                result.add( new Row(column(i)).maxLen() );
            }

            return ImmutableList.copyOf(result);

        }

        private List<String> column(int column ) {

            List<String> result = Lists.newArrayList();

            for (List<String> strings : table) {
                result.add( strings.get( column ) );
            }

            return result;

        }

    }

    /**
     * Functions for working with a row of objects stored in a list.
     */
    static class Row {

        private final Collection<String> values;

        public Row(Collection<String> values) {
            this.values = values;
        }

        public int maxLen(  ) {

            int result = 0;

            for (String value : values) {
                if ( value.length() > result ) {
                    result = value.length();
                }
            }

            return Math.max(1,result);

        }

    }


    enum Justification {
        LEFT,
        RIGHT
    }

}
