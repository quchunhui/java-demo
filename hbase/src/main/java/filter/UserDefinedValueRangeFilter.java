package filter;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterBase;

import java.io.IOException;
import java.io.Serializable;

/**
 * 自定义过滤器、实际用的时候要进行序列化
 */
public class UserDefinedValueRangeFilter extends FilterBase implements Serializable {
    private byte[] columnFamily;
    private byte[]columnQualifier;
    private String valueStart;
    private String valueEnd;
    private boolean foundColumn = false;
    private boolean matchedColumn = false;
    private boolean filterIfMissing = false;

    public UserDefinedValueRangeFilter(final byte[] family, final byte[] qualifier, final String start, final String end) {
        this.columnFamily = family;
        this.columnQualifier = qualifier;
        this.valueStart = start;
        this.valueEnd = end;
    }

    /**
     * Filters a row based on the row key. If this returns true, the entire row will be excluded. If
     * false, each KeyValue in the row will be passed to {@link #filterKeyValue(Cell)} below.
     *
     * Concrete implementers can signal a failure condition in their code by throwing an
     * {@link IOException}.
     *
     * @param buffer buffer containing row key
     * @param offset offset into buffer where row key starts
     * @param length length of the row key
     * @return true, remove entire row, false, include the row (maybe).
     * @throws IOException in case an I/O or an filter specific failure needs to be signaled.
     */
    @Override
    public boolean filterRowKey(byte[] buffer, int offset, int length) throws IOException {
        return false;
    }

    /**
     * Last chance to veto row based on previous {@link #filterKeyValue(Cell)} calls. The filter
     * needs to retain state then return a particular value for this call if they wish to exclude a
     * row if a certain column is missing (for example).
     *
     * Concrete implementers can signal a failure condition in their code by throwing an
     * {@link IOException}.
     *
     * @return true to exclude row, false to include row.
     * @throws IOException in case an I/O or an filter specific failure needs to be signaled.
     */
    @Override
    public boolean filterRow() throws IOException {
        // If column was found, return false if it was matched, true if it was not
        // If column not found, return true if we filter if missing, false if not
        return this.foundColumn? !this.matchedColumn: this.filterIfMissing;
    }

    /**
     * A way to filter based on the column family, column qualifier and/or the column value. Return
     * code is described below. This allows filters to filter only certain number of columns, then
     * terminate without matching ever column.
     *
     * If filterRowKey returns true, filterKeyValue needs to be consistent with it.
     *
     * filterKeyValue can assume that filterRowKey has already been called for the row.
     *
     * If your filter returns <code>ReturnCode.NEXT_ROW</code>, it should return
     * <code>ReturnCode.NEXT_ROW</code> until {@link #reset()} is called just in case the caller calls
     * for the next row.
     *
     * Concrete implementers can signal a failure condition in their code by throwing an
     * {@link IOException}.
     *
     * @param c the Cell in question
     * @return INCLUDE:Include the Cell
     * INCLUDE_AND_NEXT_COL:Include the Cell and seek to the next column skipping older versions.
     * SKIP:Skip this Cell.
     * NEXT_COL:Skip this column. Go to the next column in this row.
     * NEXT_ROW:Done with columns, skip to next row. Note that filterRow() will still be called.
     * SEEK_NEXT_USING_HINT:Seek to next key which is given as hint by the filter.
     * @throws IOException in case an I/O or an filter specific failure needs to be signaled.
     * @see Filter.ReturnCode
     */
    @Override
    public ReturnCode filterKeyValue(Cell c) throws IOException {
        if (!CellUtil.matchingColumn(c, this.columnFamily, this.columnQualifier)) {
            return ReturnCode.INCLUDE;
        }
        foundColumn = true;

        if (c.getValueArray() == null) {
            return ReturnCode.NEXT_ROW;
        }

        String cellValue = new String(c.getValueArray());
        if (cellValue.isEmpty()) {
            return ReturnCode.NEXT_ROW;
        }

        if (cellValue.compareTo(valueStart) < 0 || cellValue.compareTo(valueEnd) > 0) {
            return ReturnCode.NEXT_ROW;
        }

        matchedColumn = true;
        return ReturnCode.INCLUDE;
    }

    /**
     * Reset the state of the filter between rows.
     *
     * Concrete implementers can signal a failure condition in their code by throwing an
     * {@link IOException}.
     *
     * @throws IOException in case an I/O or an filter specific failure needs to be signaled.
     */
    @Override
    public void reset() throws IOException {
        foundColumn = false;
        matchedColumn = false;
    }

    /**
     * If this returns true, the scan will terminate.
     *
     * Concrete implementers can signal a failure condition in their code by throwing an
     * {@link IOException}.
     *
     * @return true to end scan, false to continue.
     * @throws IOException in case an I/O or an filter specific failure needs to be signaled.
     */
    @Override
    public boolean filterAllRemaining() throws IOException {
        return false;
    }

    /**
     * Set whether entire row should be filtered if column is not found.
     * <p>
     * If true, the entire row will be skipped if the column is not found.
     * <p>
     * If false, the row will pass if the column is not found.  This is default.
     * @param filterIfMissing flag
     */
    public void setFilterIfMissing(boolean filterIfMissing) {
        this.filterIfMissing = filterIfMissing;
    }
}
