// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.nereids.util;

import org.apache.doris.catalog.AggregateType;
import org.apache.doris.catalog.Column;
import org.apache.doris.catalog.HashDistributionInfo;
import org.apache.doris.catalog.KeysType;
import org.apache.doris.catalog.OlapTable;
import org.apache.doris.catalog.Type;
import org.apache.doris.nereids.trees.plans.logical.LogicalOlapScan;
import org.apache.doris.thrift.TStorageType;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class PlanConstructor {
    public static OlapTable student;
    public static OlapTable score;
    public static OlapTable course;

    static {
        student = new OlapTable(0L, "student",
                ImmutableList.<Column>of(new Column("id", Type.INT, true, AggregateType.NONE, "0", ""),
                        new Column("gender", Type.INT, false, AggregateType.NONE, "0", ""),
                        new Column("name", Type.STRING, true, AggregateType.NONE, "", ""),
                        new Column("age", Type.INT, true, AggregateType.NONE, "", "")),
                KeysType.PRIMARY_KEYS, null, null);
        score = new OlapTable(1L, "course",
                ImmutableList.<Column>of(new Column("sid", Type.INT, true, AggregateType.NONE, "0", ""),
                        new Column("cid", Type.INT, true, AggregateType.NONE, "", ""),
                        new Column("grade", Type.DOUBLE, true, AggregateType.NONE, "", "")),
                KeysType.PRIMARY_KEYS, null, null);
        course = new OlapTable(2L, "course",
                ImmutableList.<Column>of(new Column("cid", Type.INT, true, AggregateType.NONE, "0", ""),
                        new Column("name", Type.STRING, true, AggregateType.NONE, "", ""),
                        new Column("teacher", Type.STRING, true, AggregateType.NONE, "", "")),
                KeysType.PRIMARY_KEYS, null, null);
        student.setIndexMeta(-1,
                "base",
                student.getFullSchema(),
                0, 0, (short) 0,
                TStorageType.COLUMN,
                KeysType.PRIMARY_KEYS);
        score.setIndexMeta(-1,
                "base",
                score.getFullSchema(),
                0, 0, (short) 0,
                TStorageType.COLUMN,
                KeysType.PRIMARY_KEYS);
        course.setIndexMeta(-1,
                "base",
                course.getFullSchema(),
                0, 0, (short) 0,
                TStorageType.COLUMN,
                KeysType.PRIMARY_KEYS);
    }

    public static OlapTable newOlapTable(long tableId, String tableName, int hashColumn) {
        List<Column> columns = ImmutableList.of(
                new Column("id", Type.INT, true, AggregateType.NONE, "0", ""),
                new Column("name", Type.STRING, true, AggregateType.NONE, "", ""));

        HashDistributionInfo hashDistributionInfo = new HashDistributionInfo(3,
                ImmutableList.of(columns.get(hashColumn)));

        OlapTable table = new OlapTable(tableId, tableName, columns,
                KeysType.PRIMARY_KEYS, null, hashDistributionInfo);
        table.setIndexMeta(-1,
                "base",
                table.getFullSchema(),
                0, 0, (short) 0,
                TStorageType.COLUMN,
                KeysType.PRIMARY_KEYS);
        return table;
    }

    // With OlapTable.
    // Warning: equals() of Table depends on tableId.
    public static LogicalOlapScan newLogicalOlapScan(long tableId, String tableName, int hashColumn) {
        return new LogicalOlapScan(newOlapTable(tableId, tableName, hashColumn), ImmutableList.of("db"));
    }
}
