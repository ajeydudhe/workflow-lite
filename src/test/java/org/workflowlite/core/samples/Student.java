/********************************************************************
 * File Name:    Student.java
 *
 * Date Created: Sep 17, 2017
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2017 ajeydudhe@gmail.com
 *
 *******************************************************************/

package org.workflowlite.core.samples;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
  
public class Student
{
  public Student(final String name)
  {
    this.name = name;
  }
  
  public String getName()
  {
    return this.name;
  }

  public void addScore(final String subject, final int score)
  {
    this.scores.put(subject, score);
  }
  
  public Map<String, Integer> getScores()
  {
    return Collections.unmodifiableMap(this.scores);
  }
  
  // Private
  private final String name;
  private final Map<String, Integer> scores = new HashMap<>();
}

