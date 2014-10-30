package aki.tags

linkTagmanager = new LinkTagsManager(new SQLConnector().create())
linkTagmanager.deleteAllLinks()
linkTagmanager.insertLinks(10);


println linkTagmanager.getLinksCount();

