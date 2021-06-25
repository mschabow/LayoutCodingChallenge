# LayoutCodingChallenge
Coding Challenge: Chage X and Y values in .xml document without changing formatting

When rendering a visual object in an application you will need an exact (X,Y) coordinate to instruct the render where to paint the object.
	<Address ID="DE01PRI-OUT" ShowAddress = "FALSE">
		<NextAddr ID="BA.2">
			<Segment Type="ArcNNE">											<Start X="0" Y="495"/>
				<End X="4" Y="499"/>
			</Segment>
			<Segment Type="ArcSWS">
				<Start X="4" Y="499"/>
				<End X="8" Y="503"/>
			</Segment>
			<Segment Type="Line">
				<Start X="8" Y="503"/>
				<End X="8" Y="566"/>
			</Segment>
			<Segment Type="ArcNE">
				<Start X="8" Y="566"/>
				<End X="18" Y="576"/>
			</Segment>
			<Segment Type="Line">
				<Start X="18" Y="576"/>
				<End X="231" Y="576"/>
			</Segment>
		</NextAddr>
	</Address>

When drawing a large Factory layout these coordinates are loaded from an xml file. These files can become very large and time consuming to build or alter. It is common for these layouts to change as factories expand or equipment is moved to a new location.

Your solution should allow the user to shift the layout right, left, up, or down by a given number of pixels. For example, if the user wanted the entire layout shifted 10 px to the right you would add 10 to all of the X=”??” xml elements in the text file. Or, if they wanted the layout shifted UP 25 px you would add 25 to all the Y=”??” xml elements in the text file. 

