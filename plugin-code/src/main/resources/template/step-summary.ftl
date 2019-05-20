<#--
Copyright 2018 trivago N.V.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

<#import "macros/page.ftl"as page>
<#import "macros/common.ftl" as common>
<#import "macros/navigation.ftl" as navigation>

<@page.page
base=".."
links=["feature_summary", "tag_summary", "scenario_sequence", "scenario_summary"]
headline="All Steps"
subheadline=""
preheadline=""
preheadlineLink="">

    <div class="row">
        <@page.card width="8" title="Step Summary Result Chart" subtitle="" classes="">
            <@page.graph />
        </@page.card>
        <@page.card width="4" title="Steps Summary" subtitle="" classes="">
            <ul class="list-group list-group-flush" data-cluecumber-item="step-summary">
                <li class="list-group-item">${totalNumberOfSteps} Step(s) in<br>
                    ${totalNumberOfScenarios} Scenario(s)
                </li>
            </ul>
            <li class="list-group-item">
                ${totalNumberOfPassed} <@common.status status="passed"/>
                ${totalNumberOfFailed} <@common.status status="failed"/>
                ${totalNumberOfSkipped} <@common.status status="skipped"/>
            </li>
        </@page.card>
    </div>
    <script>
   $(document).ready(function() {
   $('.renderAsDataTable').dataTable().api().column(1).visible(false);
   $('.renderAsDataTable').dataTable().api().column(2).visible(false);
   var select = $('#tagfilter').on( 'change', function () {
                        var val = $(this).val();
                         
                        $('.renderAsDataTable').dataTable().api()
                            .column(2)
                            .search( val )
                            .draw();
                });
 
   var select = $('#gluefilter').on( 'change', function () {
                        var val = $(this).val();
 
                        $('.renderAsDataTable').dataTable().api()
                            .column(1)
                            .search( val )
                            .draw();
                });
 
  });
    </script>
    <div class="row">
        <@page.card width="12" title="Available Steps" subtitle="" classes="">
            Filter by Class: 
            <select id="gluefilter">
                <option></option>
                <#list allGlueClasses as glueClass>
                <option>${glueClass}</option>
                </#list>
            </select>
            Filter by Tag: 
            <select id="tagfilter">
                <option></option>
                <#list allTags as tag>
                <option>${tag.name}</option>
                </#list>
            </select>
            <table id="step_summary" class="table table-hover renderAsDataTable"
                   data-cluecumber-item="step-summary-table">
                <thead>
                <tr>
                    <th>Step</th>
                    <th style="dislay:none;">Glue Code</th>
                    <th style="dislay:none;">Tags</th>
                    <th>Total</th>
                    <th><@common.status status="passed"/></th>
                    <th><@common.status status="failed"/></th>
                    <th><@common.status status="skipped"/></th>
                    <th>Min Time</th>
                    <th>Max Time</th>
                    <th>Ø Time</th>
                </tr>
                </thead>
                <tbody>
                <#list stepResultCounts as step, stepResultCount>
                    <tr>
                        <td class="text-left">
                            <span data-toggle="tooltip" title="${step.glueMethodName}">
                                <a href="pages/step-scenarios/step_${step.getUrlFriendlyName()}.html">${step.returnNameWithArgumentPlaceholders()}</a>
                            </span>
                        </td>
                        <td class="text-left" style="dislay:none;">${step.glueMethodName}</td>
                        <td class="text-left" style="dislay:none;"><#list getTagsFromStep(step) as tag>${tag.name} </#list></td>
                        <td class="text-right"><strong>${stepResultCount.total}</strong></td>
                        <td class="text-right">${stepResultCount.passed}</td>
                        <td class="text-right">${stepResultCount.failed}</td>
                        <td class="text-right">${stepResultCount.skipped}</td>
                        <td class="text-right small">
                            <#if (getMinimumTimeScenarioIndexFromStep(step) > -1)>
                                <a href="pages/scenario-detail/scenario_${getMinimumTimeScenarioIndexFromStep(step)}.html">${getMinimumTimeFromStep(step)}</a>
                            </#if>
                        </td>
                        <td class="text-right small">
                            <#if (getMaximumTimeScenarioIndexFromStep(step) > -1)>
                                <a href="pages/scenario-detail/scenario_${getMaximumTimeScenarioIndexFromStep(step)}.html">${getMaximumTimeFromStep(step)}</a>
                            </#if>
                        </td>
                        <td class="text-right small">${getAverageTimeFromStep(step)}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </@page.card>
    </div>
</@page.page>
