# CustomViewPlayground
This project has created with learning purposes and demonstrate how custom views can be implemented.
There is the result:

1. Portrait

![](https://media.giphy.com/media/3ohs4sbWgmK9jvH7a0/giphy.gif)

2. Landscape

![](https://media.giphy.com/media/26DMWj4Uz6pNhXxh6/giphy.gif)

There are three the most important callbacks in view:

  onMeasure<br />
  onLayout<br />
  onDraw
  
In this project in TreeViewGroup.class were implemented onMeasure and onLayout callbacks. 
Because TreeView Group.class is a child of ViewGroup and this object holds another views, measuring and layouting were delegated to it.
So depend on a orientation TreeViewGroup.class will change views location as on the images above.

