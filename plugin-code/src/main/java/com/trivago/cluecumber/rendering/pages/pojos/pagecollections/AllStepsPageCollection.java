/*
 * Copyright 2018 trivago N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trivago.cluecumber.rendering.pages.pojos.pagecollections;

import com.trivago.cluecumber.constants.PluginSettings;
import com.trivago.cluecumber.json.pojo.Element;
import com.trivago.cluecumber.json.pojo.Report;
import com.trivago.cluecumber.json.pojo.Step;
import com.trivago.cluecumber.json.pojo.Tag;
import com.trivago.cluecumber.rendering.pages.pojos.ResultCount;
import com.trivago.cluecumber.rendering.pages.pojos.Times;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AllStepsPageCollection extends ScenarioSummaryPageCollection {
    private Map<Step, ResultCount> stepResultCounts = new HashMap<>();
    private Map<Step, Times> stepTimes = new HashMap<>();
    private Map<Step, List<Tag>> stepTags = new HashMap<>();
    private List<Tag> allTags = new ArrayList<>();
    private List<String> allGlueClasses = new ArrayList<>();

    public AllStepsPageCollection(List<Report> reports) {
        super(PluginSettings.STEP_SUMMARY_PAGE_NAME);
        calculateStepResultCounts(reports);
    }

    /**
     * Get a map of {@link ResultCount} lists connected to step names.
     *
     * @return a map of {@link ResultCount} lists with steps as keys.
     */
    public Map<Step, ResultCount> getStepResultCounts() {
        return stepResultCounts;
    }

    public Set<Step> getSteps() {
        return stepResultCounts.keySet();
    }

    public int getTotalNumberOfSteps() {
        return stepResultCounts.size();
    }

    public String getMinimumTimeFromStep(final Step step) {
        return stepTimes.get(step).getMinimumTimeString();
    }

    public int getMinimumTimeScenarioIndexFromStep(final Step step) {
        return stepTimes.get(step).getMinimumTimeScenarioIndex();
    }

    public String getMaximumTimeFromStep(final Step step) {
        return stepTimes.get(step).getMaximumTimeString();
    }

    public int getMaximumTimeScenarioIndexFromStep(final Step step) {
        return stepTimes.get(step).getMaximumTimeScenarioIndex();
    }

    public String getAverageTimeFromStep(final Step step) {
        return stepTimes.get(step).getAverageTimeString();
    }

    public List<Tag> getTagsFromStep(final Step step) {
        return stepTags.get(step);
    }
    
    public List<Tag> getAllTags() {    	
    	return allTags;
    }

    public List<String> getAllGlueClasses() {
    	
    	return allGlueClasses;    	
    }

    /**
     * Calculate the numbers of failures, successes and skips per step.
     *
     * @param reports The {@link Report} list.
     */
    private void calculateStepResultCounts(final List<Report> reports) {
        if (reports == null) return;
        
        Set<Tag> allTagsSet =  new HashSet<>();
        Set<String> allGlueSet =  new HashSet<>();
        
        for (Report report : reports) {
            for (Element element : report.getElements()) {
                int scenarioIndex = element.getScenarioIndex();
            	allTagsSet.addAll(element.getTags());
                for (Step step : element.getSteps()) {
                	if (step.getGlueMethodName() != null && step.getGlueMethodName().contains(".")) {
                	   allGlueSet.add(step.getGlueMethodName().split("\\.")[0]);
                	}
                	if (element.getTags() != null) {
                	   stepTags.put(step, element.getTags());
                	}
                    ResultCount stepResultCount = stepResultCounts.getOrDefault(step, new ResultCount());
                    updateResultCount(stepResultCount, step.getStatus());
                    stepResultCounts.put(step, stepResultCount);
                    addScenarioIndexByStatus(element.getStatus(), element.getScenarioIndex());
                    Times stepTimes = this.stepTimes.getOrDefault(step, new Times());
                    if (!step.isSkipped()) {
                        stepTimes.addTime(step.getResult().getDuration(), scenarioIndex);
                    }
                    this.stepTimes.put(step, stepTimes);
                }
            }
        }
        
        allTags = allTagsSet.stream().sorted((t1, t2) -> t1.getName().compareTo(t2.getName())).collect(Collectors.toCollection(ArrayList::new));
        allGlueClasses = allGlueSet.stream().sorted().collect(Collectors.toCollection(ArrayList::new));
    }
}
