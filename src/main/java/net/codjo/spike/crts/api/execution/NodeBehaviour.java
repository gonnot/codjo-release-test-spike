/*
 * codjo (Prototype)
 * =================
 *
 *    Copyright (C) 2012, 2012 by codjo.net
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *    implied. See the License for the specific language governing permissions
 *    and limitations under the License.
 */

package net.codjo.spike.crts.api.execution;
/**
 *
 */
public interface NodeBehaviour {
    /*
    public interface Tag extends javax.servlet.jsp.tagext.JspTag {
        int SKIP_BODY = 0;
        int EVAL_BODY_INCLUDE = 1;
        int SKIP_PAGE = 5;
        int EVAL_PAGE = 6;

        void setPageContext(javax.servlet.jsp.PageContext pageContext);

        void setParent(javax.servlet.jsp.tagext.Tag aaaaa);

        javax.servlet.jsp.tagext.Tag getParent();

        int doStartTag() throws javax.servlet.jsp.JspException;

        int doEndTag() throws javax.servlet.jsp.JspException;

       // Local (when father tag is done) or global (when script is done) -> inject via context
        void release();
    }
     */
    public void run(ExecutionContext context) throws Exception;
}
