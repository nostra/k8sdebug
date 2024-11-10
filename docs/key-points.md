# Summary

Caveat emptor: The setup in this repository is not a production ready setup, as I 
have done a lot of shortcuts with regard to security tightening. With a tighter 
setup, you might find that some of the techniques are no longer applicable.

- There are many ways you can debug. You now have more options to which method you can use
- Soak testing is a nifty way of introducing load
- Metrics rule
    - Good and representative metrics, can be used in a production dashboard too  
- Security profiles may give you grief, as you would need to restart the application to adjust settings
    - Plan ahead for potential trouble
    - Remember to remove temporary access rights when not needed
- Which method you can use for introspection of a container depends on the Kubernetes flavor, and how the image is build / set up
    - It can be taxing to figure out how to attach a container with the correct setup 
