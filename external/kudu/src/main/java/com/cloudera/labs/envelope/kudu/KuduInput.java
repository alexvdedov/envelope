/**
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudera.labs.envelope.kudu;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import com.cloudera.labs.envelope.input.BatchInput;
import com.cloudera.labs.envelope.load.ProvidesAlias;
import com.cloudera.labs.envelope.spark.Contexts;
import com.typesafe.config.Config;

public class KuduInput implements BatchInput, ProvidesAlias {

  public static final String CONNECTION_CONFIG_NAME = "connection";
  public static final String TABLE_NAME_CONFIG_NAME = "table.name";

  private Config config;

  @Override
  public void configure(Config config) {
    this.config = config;
  }

  @Override
  public Dataset<Row> read() throws Exception {
    String connection = config.getString(CONNECTION_CONFIG_NAME);
    String tableName = config.getString(TABLE_NAME_CONFIG_NAME);

    Dataset<Row> tableDF = Contexts.getSparkSession().read()
                                                     .format("org.apache.kudu.spark.kudu")
                                                     .option("kudu.master", connection)
                                                     .option("kudu.table", tableName)
                                                     .load();

    return tableDF;
  }

  @Override
  public String getAlias() {
    return "kudu";
  }
}
