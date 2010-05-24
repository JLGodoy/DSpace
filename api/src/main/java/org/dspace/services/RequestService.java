/**
 * $Id$
 * $URL$
 * *************************************************************************
 * Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 * Licensed under the DuraSpace License.
 *
 * A copy of the DuraSpace License has been included in this
 * distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
 */
package org.dspace.services;

import org.dspace.services.model.RequestInterceptor;

/**
 * Allows for the managing of requests in the system in a way which is 
 * independent of any underlying system or code.
 * 
 * @author Aaron Zeckoski (azeckoski @ gmail.com)
 */
public interface RequestService {

    /**
     * Initiates a request in the system.
     * Normally this would be triggered by a servlet request starting.
     * <p>
     * Only one request can be associated with the current thread, so if 
     * another one is running it will be destroyed and a new one will be 
     * created.
     * <p>
     * Note that requests are expected to be manually ended somehow and 
     * will not be closed out automatically.
     * 
     * @return the unique generated id for the new request
     * @throws IllegalArgumentException if the session is null, invalid, or there is no current session
     */
    public String startRequest();

    /**
     * Ends the current running request.  This can indicate success or
     * failure of the request.  This will trigger any interceptors and
     * normally would be caused by a servlet request ending.
     * Note that a request cannot be ended twice:  once it is ended this
     * will just return null.
     * 
     * @param failure (optional) this is the exception associated with 
     * the failure.  Leave as null if the request is ending successfully.
     * You can make up a {@link RuntimeException} if you just need to 
     * indicate that the request failed.
     * @return the request ID if the request closes successfully and is 
     * not already closed OR null if there is no current request.
     */
    public String endRequest(Exception failure);

    /**
     * Finds out of there is a request running in this thread and if so 
     * what the id of that request is.
     * 
     * @return the id of the current request for this thread OR null if there is not one
     */
    public String getCurrentRequestId();

    /**
     * Allows developers to perform actions on the start and end of the 
     * request cycle.  If you decide you do not need to use your
     * interceptor anymore then simply destroy it (dereference it) and 
     * the service will stop calling it.  This means that you should
     * keep a reference to your interceptor when creating it
     * (like an inline class or registerRequestListener(new 
     * YourInterceptor())) or will be destroyed immediately.
     * This registration is ClassLoader safe.
     * 
     * @param interceptor an implementation of {@link RequestInterceptor}
     * @throws IllegalArgumentException if this priority is invalid or the input is null
     */
    public void registerRequestInterceptor(RequestInterceptor interceptor);

}
