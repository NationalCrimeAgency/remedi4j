/*
National Crime Agency (c) Crown Copyright 2018

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/**
 * Remedi4J contains classes and utilities for working with requests and
 * responses from the REMEDI machine translation servers
 * (https://github.com/ivan-zapreev/Distributed-Translation-Infrastructure)
 */
module uk.gov.nca.remedi4j {
  exports uk.gov.nca.remedi4j.data;
  exports uk.gov.nca.remedi4j.exceptions;
  exports uk.gov.nca.remedi4j.utils;
  exports uk.gov.nca.remedi4j.client;

  requires com.fasterxml.jackson.annotation;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires icu4j;
  requires java.net.http;
  requires slf4j.api;
}