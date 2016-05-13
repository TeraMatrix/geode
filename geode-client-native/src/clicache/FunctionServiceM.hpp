/*=========================================================================
 * Copyright (c) 2002-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */

#pragma once

#include "gf_defs.hpp"
#include "cppcache/FunctionService.hpp"
#include "CacheM.hpp"
#include "impl/NativeWrapper.hpp"


using namespace System;

namespace GemStone
{
  namespace GemFire
  {
    namespace Cache
    {

      ref class Region;
      ref class Pool;
      ref class Execution;

      /// <summary>
      /// A factory class used to create Execute object for function execution
      /// </summary>
      /// <remarks>
      [Obsolete("Use classes and APIs from the GemStone.GemFire.Cache.Generic namespace")]
      public ref class FunctionService 
        : public Internal::SBWrap<gemfire::FunctionService>
      {
      public:

        /// <summary>
        /// Creates a new region Execution object
        /// </summary>
        /// <remarks>
        /// If Pool is multiusersecure mode then one need to pass logical instance of Region Pool->CreateSecureUserCache(<credentials>)->getRegion(<regionPath>).
        /// </remarks>
        static Execution^ OnRegion( Region^ rg );

        /// <summary>
        /// Creates a new Execution object on one server
        /// </summary>
        /// <remarks>
        /// </remarks>
        /// <exception cref="UnsupportedOperationException">unsupported operation exception, when Pool is in multiusersecure mode.</exception>
        static Execution^ OnServer( Pool^ pl );

        /// <summary>
        /// Creates a new Execution object on all servers in the pool
        /// </summary>
        /// <remarks>
        /// </remarks>
        /// <exception cref="UnsupportedOperationException">unsupported operation exception, when Pool is in multiusersecure mode.</exception>
        static Execution^ OnServers( Pool^ pl );

        /// <summary>
        /// Creates a new Execution object on one server.
        /// </summary>
        /// <remarks>
        /// </remarks>
        /// <exception cref="IllegalStateException">when Pool has been closed.</exception>
        static Execution^ OnServer( IRegionService^ cache );

        /// <summary>
        /// Creates a new Execution object on all servers in the pool.
        /// </summary>
        /// <remarks>
        /// </remarks>
        /// <exception cref="IllegalStateException">when Pool has been closed.</exception>
        static Execution^ OnServers( IRegionService^ cache );
        
      internal:

        /// <summary>
        /// Private constructor to wrap a native object pointer
        /// </summary>
        /// <param name="nativeptr">The native object pointer</param>
        inline FunctionService( gemfire::FunctionService* nativeptr )
          : SBWrap( nativeptr ) { }

      };

    }
  }
}