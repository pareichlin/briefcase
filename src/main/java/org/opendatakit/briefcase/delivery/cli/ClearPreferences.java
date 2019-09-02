/*
 * Copyright (C) 2018 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opendatakit.briefcase.delivery.cli;

import static java.util.Comparator.naturalOrder;
import static org.opendatakit.briefcase.delivery.cli.Common.WORKSPACE_LOCATION;

import java.util.List;
import java.util.stream.Stream;
import org.opendatakit.briefcase.delivery.ui.export.ExportPanel;
import org.opendatakit.briefcase.delivery.ui.transfer.pull.PullPanel;
import org.opendatakit.briefcase.delivery.ui.transfer.push.PushPanel;
import org.opendatakit.briefcase.reused.cli.Operation;
import org.opendatakit.briefcase.reused.cli.OperationBuilder;
import org.opendatakit.briefcase.reused.cli.Param;
import org.opendatakit.briefcase.reused.model.preferences.BriefcasePreferences;

public class ClearPreferences {

  private static Param<Void> CLEAR = Param.flag("c", "clear_prefs", "Clear saved preferences");

  public static Operation create() {
    return new OperationBuilder()
        .withFlag(CLEAR)
        .withOptionalParams(WORKSPACE_LOCATION)
        .withLauncher(args -> clear())
        .build();
  }

  private static void clear() {
    flush(BriefcasePreferences.appScoped());
    Stream.of(
        PullPanel.class,
        PushPanel.class,
        ExportPanel.class
    ).map(BriefcasePreferences::forClass)
        .forEach(ClearPreferences::flush);
  }

  private static void flush(BriefcasePreferences appPreferences) {
    System.out.println("Clearing saved keys on " + appPreferences.node);
    List<String> keys = appPreferences.keys();
    appPreferences.removeAll(keys);
    keys.sort(naturalOrder());
    keys.forEach(key -> System.out.println("  " + key));
  }

}
