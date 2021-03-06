.. _start:

###########################
Getting Started With Galaxy
###########################

.. _start-requirements:

System requirements
===================

Galaxy requires that the Java Runtime Environment (JRE) version 7 will be installed on your machine.

.. _start-download:

Downloading Galaxy
==================

You can download the Galaxy distribution from here:
  * `Release 1.0`__
__ http://paralleluniverse.co/docs/galaxy-1.0-SNAPSHOT.zip


.. _start-build:

Building Galaxy
===============

To build galaxy, simply ``cd`` to the Galaxy directory, then run:

.. code-block:: sh

  gradle

If you don't have gradle installed on your machine then run instead:

.. code-block:: sh

  ./gradlew

To build the documentation, you need to have Sphinx_ and lessc_ installed. Then run:

.. code-block:: sh

  gradle generateDocs

.. _Sphinx: http://sphinx.pocoo.org/
.. _lessc: http://lesscss.org/


.. _start-maven:

Using Maven
===========

Galaxy will be available on Maven Central very soon!

.. _start-config:

Using the pre-built configurations
==================================

The user manual explains, at length, how to configure Galaxy (see :ref:`man-config`).
However, for your convenience, a number of pre-built configurations are included with the Galaxy
distribution, and can be found in the ``config`` directory.

While normally you'd have one large XML file with Galaxy's configuration, 
these sample configurations are divided into snippet XML files (those XML files starting with an underscore in the
``config`` directory) that you can mix and match.

Peer configuration
------------------

Regular Galaxy cluster nodes are called "peers", and to configure a peer, take a look at ``peer.xml`` in the ``config``
directory. It contains three or four XML ``import`` elements:

1. ``_peer.xml``. This import is required for all peer configurations.

2. Cluster management product. Can be one of:

   * ``_jgroups.xml`` - to use JGroups.

   * ``_zookeeper.xml`` - to use Apache ZooKeeper. If selected, configure ``zkConnectString`` in ``_zookeeper.xml`` to 
     match your ZooKeeper configuration.

3. One of: 

   * ``_with_server.xml`` - if you'd like your cluster to have a server node for persistence 
     (see :ref:`man-config-server` for an explanation about servers).

   * ``_with_cloud_server.xml`` - if you want a server but are running Galaxy in a cloud environment that does not allow multicast.
     In that case, you must use ``_zookeeper.xml``.

   * ``_with_dumb_server`` - if you'd like to use a server that isn't a Galaxy node but a simple SQL database for persistence.
     In this case, you must also import ``_sql.xml`` as item 4.
     (see :ref:`man-config-server-dumb` for an explanation about dumb servers).

   * ``_no_server.xml``- if you don't want a server at all.

4. Possibly ``_sql.xml``, if and only if you've chosen `_with_dumb_server``.
   If chosen, edit ``_sql.xml`` with your database connection information.
   See :ref:`man-config-server-store-jdbc` for more information about configuring the RDBMS integration.

.. note::
  
  It is recommended that you configure your cluster to use a server node or a dumb server.
  See :ref:`man-config-cluster-organization-serverless`.

In addition to this file, you'll need to edit an additional ``.properties`` file. You must provide these properties
(you can edit ``peer.properties`` in the ``config`` directory):

1. ``galaxy.nodeId`` - this will identify the node in the cluster. Two or more nodes with the same id will form
   a "backup group" (see :ref:`man-config-cluster-organization-backup`).

2. ``galaxy.port`` - this is the UDP port that Galaxy will use to send messages among peer nodes.

3. ``galaxy.slave_port`` - if there are more than one nodes with the same id, the slaves of the peer group will connect
   to this TCP port on the master to receive backup data.

4. ``galaxy.multicast.address`` - the IP address to use for multicast (not used when ``_with_cloud_server`` is chosen).

5. ``galaxy.multicast.port`` - the IP port to use for multicast (not used when ``_with_cloud_server`` is chosen).

Properties 4 and 5 must be the same for all peer nodes. Properties 2 and 3 may be different for each node (this is
especially useful when running several nodes on the same machine for testing). The ``nodeId`` property should be different
for each node (but the same for nodes in the same backup group).

.. note::

  If you create several peer properties files with different ports, you can run several peers on a single machine!

Server configuration
--------------------

If you heed our recommendation and want to use Galaxy with a server node (or more, though, currently, only a single
server node is supported), you're going to need to configure it. Just like with peers, you should start by looking
at the ``server.xml`` file that's in the ``config`` directory. It is comprised of three XML ``import`` elements:

1. ``_server.xml``. This import is required for all peer configurations.

2. Cluster management product. Can be one of:
   * ``_jgroups.xml`` - to use JGroups.

   * ``_zookeeper.xml`` - to use ZooKeeper. If selected, configure ``zkConnectString`` in ``_zookeeper.xml`` to match your 
     ZooKeeper configuration.

3. Persistence layer. Can be one of:
   
   * ``_bdb.xml`` - to use Berkeley DB, Java Edition as the persistence engine.
     If you choose to use BDB JE, you might want to change the ``envHome`` property, defined in the
     ``_bdb.xml`` file, to point to the directory where you want to place the BDB files, and the ``truncate`` property
     (which can be ``true`` or false``) depending on whether or not you want the database truncated (cleared) upon server startup.
     See :ref:`man-config-server-store-bdb` for more information about configuring BDB.

   * ``_sql.xml`` - to use a SQL database with a JDBC driver for persistence.
     If chosen, edit ``_sql.xml`` with your database connection information.
     See :ref:`man-config-server-store-jdbc` for more information about configuring the RDBMS integration.

     Because you're using a server node and peers would need to access it over the network, it's best that you run
     the server on the same machine running the DB to save the extra network hop. This should actually provide better
     performance than using the DB as a dumb server as explained above.

For the server, too, you'll need to edit ``server.properties`` in the ``config`` directory. Leave ``galaxy.nodeId`` set
to ``0`` - this is what identifies the node as the server. But set ``galaxy.port`` to the TCP port
you want the peers to use when connecting to the server.

.. attention::

  Galaxy is currently in ALPHA and considered experimental. Not all configurations have been thoroughly tested, and
  some configuration combinations haven't been tested at all.

  Please submit bug reports (and feature requests) to the `issue tracker`_.

.. _`issue tracker`: https://github.com/puniverse/galaxy/issues

.. _start-run:

Running Galaxy
==============

The Galaxy server runs as a standalone process. The peers are your application code that calls into the Galaxy library.
Note that if you're using ZooKeeper, you must start the ZooKeeper servers before starting any Galaxy nodes.

Running the server
------------------

To run the server, simply run the executable Java class ``co.paralleluniverse.galaxy.Server``, and pass it two command-line 
arguments: the XML configuration file and the properties file, like so:

.. code-block:: sh

  java -classpath galaxy.jar co.paralleluniverse.galaxy.Server config/server.xml config/server.properties

Using the peers
---------------

In your application code, you need to get an instance of the ``Grid`` class (see :ref:man-api`),
which is the entry point to Galaxy's API. You do it by calling the ``getInstance`` static method, and passing it 
the location of the XML and properties files you have configured in the :ref:`configuration step <start-config>`,
like this:

.. code-block:: java

    Grid grid = Grid.getInstance("config/peer.xml", "config/peer1.properties");

(depending on your current directory you may need to provide a different path to the XML and properties files.)

Usually, your next statement would be to tell the node to go online:

.. code-block:: java

    grid.goOnline();

Now you should read the :ref:`API section <man-api>` of the manual to learn how to use the Galaxy API.




