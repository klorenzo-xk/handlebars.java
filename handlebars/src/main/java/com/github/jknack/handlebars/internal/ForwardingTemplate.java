/**
 * Copyright (c) 2012-2013 Edgar Espina
 *
 * This file is part of Handlebars.java.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jknack.handlebars.internal;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.TagType;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.TypeSafeTemplate;

/**
 * A forwarding template implementation.
 *
 * @author edgar.espina
 * @since 0.11.0
 */
class ForwardingTemplate implements Template {

  /**
   * The original template.
   */
  private final Template template;

  /**
   * Creates a new {@link ForwardingTemplate}.
   *
   * @param template The original template. Required.
   */
  public ForwardingTemplate(final Template template) {
    this.template = notNull(template, "The template is required.");
  }

  @Override
  public void apply(final Object context, final Writer writer) throws IOException {
    apply(wrap(context), writer);
  }

  @Override
  public String apply(final Object context) throws IOException {
    return apply(wrap(context));
  }

  @Override
  public void apply(final Context context, final Writer writer) throws IOException {
    Context wrappedContext = wrap(context);
    try {
      beforeApply(wrappedContext);
      template.apply(wrappedContext, writer);
    } finally {
      afterApply(wrappedContext);
      if (wrappedContext != context) {
        wrappedContext.destroy();
      }
    }
  }

  @Override
  public String apply(final Context context) throws IOException {
    Context wrappedContext = wrap(context);
    try {
      beforeApply(wrappedContext);
      return template.apply(wrappedContext);
    } finally {
      afterApply(wrappedContext);
      if (wrappedContext != context) {
        wrappedContext.destroy();
      }
    }
  }

  /**
   * Call it after a template has been applied.
   *
   * @param context The template context.
   */
  protected void afterApply(final Context context) {
  }

  /**
   * Call it before a template has been applied.
   *
   * @param context The template context.
   */
  protected void beforeApply(final Context context) {
  }

  @Override
  public String text() {
    return template.text();
  }

  @Override
  public String toJavaScript() throws IOException {
    return template.toJavaScript();
  }

  @Override
  public <T, S extends TypeSafeTemplate<T>> S as(final Class<S> type) {
    return template.as(type);
  }

  @Override
  public <T> TypeSafeTemplate<T> as() {
    return template.as();
  }

  @Override
  public String toString() {
    return template.toString();
  }

  /**
   * Wrap the candidate object as a Context, or creates a new context.
   *
   * @param candidate The candidate object.
   * @return A context.
   */
  private static Context wrap(final Object candidate) {
    if (candidate instanceof Context) {
      return (Context) candidate;
    }
    return Context.newContext(candidate);
  }

  @Override
  public List<String> collect(final TagType... tagType) {
    return template.collect(tagType);
  }
}
